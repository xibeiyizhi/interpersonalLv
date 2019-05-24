package club.own.site.controller;

import club.own.site.bean.Img;
import club.own.site.bean.Member;
import club.own.site.bean.Quotation;
import club.own.site.config.redis.RedisClient;
import club.own.site.constant.ProjectConstant;
import club.own.site.utils.FileUploadUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static club.own.site.constant.ProjectConstant.MEMBER_LIST_KEY;
import static club.own.site.constant.ProjectConstant.MEMBER_TAGS_KEY;
import static club.own.site.utils.FileUploadUtils.getBasePath;

@Slf4j
@Controller
public class MemberController extends BaseController {

    @Autowired
    private RedisClient redisClient;

    @PostMapping(value = "member/add")
    public @ResponseBody String add(Member member, HttpServletRequest request){
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("photoArr");
        String relativePath = "static/img/welcome/";
        if (CollectionUtils.isNotEmpty(files)) {
            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) {
                    continue;
                }
                String fileUrl = relativePath + file.getOriginalFilename();
                if (StringUtils.isBlank(member.getFirstImgUrl())){
                    member.setFirstImgUrl(fileUrl);
                }
                String dirPath = getBasePath() + relativePath;
                FileUploadUtils.uploadFile(file, dirPath, file.getOriginalFilename());
            }
            if(member != null && StringUtils.isNotBlank(member.getMobile())) {
                try{
                    redisClient.hset(MEMBER_LIST_KEY, member.getMobile(), JSON.toJSONString(member));
                } catch (Exception e) {
                    log.error("error:redis store fail", e);
                    return "error:redis store fail";
                }
            }
        }
        return "OK";
    }

    @GetMapping(value = "member/list")
    public @ResponseBody String list() {
        Map<String, String> members = redisClient.hgetAll(MEMBER_LIST_KEY);
        List<String> values = Lists.newArrayList(members.values());
        List<Member> res = Lists.newArrayList();
        values.forEach(s -> {
            if (StringUtils.isNotBlank(s)) {
                Member member = JSON.parseObject(s, Member.class);
                try {
                    member.setName(URLEncoder.encode(member.getName(), Charsets.UTF_8.name()));
                    member.setMessage(URLEncoder.encode(member.getMessage(), Charsets.UTF_8.name()));
                    if (StringUtils.isNotBlank(member.getAddress())) {
                        member.setAddress(URLEncoder.encode(member.getAddress(), Charsets.UTF_8.name()));
                    }
                    if (StringUtils.isNotBlank(member.getProfession())) {
                        member.setProfession(URLEncoder.encode(member.getProfession(), Charsets.UTF_8.name()));
                    }
                    if (StringUtils.isNotBlank(member.getFirstImgUrl())) {
                        member.setFirstImgUrl(URLEncoder.encode(member.getFirstImgUrl(), Charsets.UTF_8.name()));
                    }
                } catch (UnsupportedEncodingException e) {
                    log.error("URLEncoder.encode error", e);
                }
                res.add(member);
            }
        });
        res.sort(Comparator.comparing(Member::getId).reversed());
        return JSON.toJSONString(res);
    }

    @GetMapping(value = "member/detail")
    public ModelAndView detail(@RequestParam(value = "phone") String phone) {
        String memberJson = redisClient.hget(MEMBER_LIST_KEY, phone);
        Member member = JSON.parseObject(memberJson, Member.class);
        ModelAndView mav = new ModelAndView();
        List<Img> imgList = new ArrayList<>();
        List<String> tagList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(member.getPhotos())) {
            for (int i = 0; i< member.getPhotos().size(); i++) {
                Img img = new Img(member.getPhotos().get(i), "pic" + (i+1));
                imgList.add(img);
            }
        }
        String tags = redisClient.hget(MEMBER_TAGS_KEY, phone);
        tags = StringUtils.isNotBlank(tags) ? tags : "";
        String regex = "\\|";
        tagList = Lists.newArrayList(tags.split(regex));

        mav.addObject("imgList", imgList);
        mav.addObject("tagList", tagList);
        mav.setViewName("detail");
        return mav;
    }

    @PostMapping(value = "member/upload")
    public @ResponseBody String editSave(@RequestParam(value = "phone") String phone,
                                         HttpServletRequest request) {
        String memberJson = redisClient.hget(MEMBER_LIST_KEY, phone);
        Member member = JSON.parseObject(memberJson, Member.class);
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("photoArr");
        String relativePath = "static/img/detail/" + phone + "/";
        if (CollectionUtils.isNotEmpty(files)) {
            int photoCount = member.getPhotos().size();
            for (int i=0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                if (file == null || file.isEmpty()) {
                    continue;
                }
                int idx = (i + photoCount) % 10;
                String desFileName = (idx + 1) + ".jpg";
                String fileUrl = relativePath + desFileName;
                if (!member.getPhotos().contains(fileUrl)) {
                    member.getPhotos().add(idx, fileUrl);
                }
                String dirPath = getBasePath() + relativePath;
                FileUploadUtils.uploadFile(file, dirPath, desFileName);
            }
        }
        if(member != null && StringUtils.isNotBlank(phone)) {
            try{
                redisClient.hset(MEMBER_LIST_KEY, member.getMobile(), JSON.toJSONString(member));
            } catch (Exception e) {
                log.error("error:redis store fail", e);
                return "error:redis store fail";
            }
        }

        return "OK";
    }

    @GetMapping(value = "member/clearphoto")
    public @ResponseBody String clearPhoto (@RequestParam(value = "phone") String phone){
        String memberJson = redisClient.hget(MEMBER_LIST_KEY, phone);
        Member member = JSON.parseObject(memberJson, Member.class);
        member.setPhotos(Lists.newArrayList());
        try{
            redisClient.hset(MEMBER_LIST_KEY, member.getMobile(), JSON.toJSONString(member));
        } catch (Exception e) {
            log.error("error:redis store fail", e);
            return "error:redis store fail";
        }
        return "OK";
    }

    @GetMapping(value = "member/tagsave")
    public @ResponseBody String tagSave (@RequestParam(value = "phone") String phone, @RequestParam(value = "tags") String tags){
        if (StringUtils.isNotBlank(tags)) {
            redisClient.hset(MEMBER_TAGS_KEY, phone, tags);
        }
        return "OK";
    }
}
