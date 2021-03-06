package club.own.site.controller;

import club.own.site.bean.Apply;
import club.own.site.bean.Img;
import club.own.site.bean.Member;
import club.own.site.config.redis.RedisClient;
import club.own.site.utils.EncodeUtils;
import club.own.site.utils.FileUploadUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static club.own.site.constant.ProjectConstant.*;
import static club.own.site.utils.FileUploadUtils.getBasePath;
import static club.own.site.utils.FileUploadUtils.getFileType;
import static club.own.site.utils.NumberUtils.hideNum;

@Slf4j
@Controller
public class MemberController extends BaseController {

    @Autowired
    private RedisClient redisClient;

    @GetMapping(value = "member/apply")
    public ModelAndView apply (){
        ModelAndView mav = new ModelAndView();
        Map<String, String> map = redisClient.hgetAll(MEMBER_ADD_APPLY_KEY);
        List<String> list = Lists.newArrayList(map.values());
        mav.addObject("applyList", list);
        mav.setViewName("apply");
        return mav;
    }

    @GetMapping(value = "member/agree")
    public @ResponseBody String agree (@RequestParam(value = "agree") boolean agree,
                                       @RequestParam(value = "phone") String phone){

        String applyMemJson = redisClient.hget(MEMBER_ADD_APPLY_KEY, phone);
        Apply applyMem = JSON.parseObject(applyMemJson, Apply.class);
        applyMem.setAgree(agree);
        redisClient.hset(MEMBER_ADD_APPLY_KEY, phone, JSON.toJSONString(applyMem));

        return "OK";
    }

    @PostMapping(value = "member/add")
    public @ResponseBody String add(Member member, HttpServletRequest request){
        String phone = member.getMobile();
        String apply = redisClient.hget(MEMBER_ADD_APPLY_KEY, phone);
        if (StringUtils.isBlank(apply)) {
            Apply applyMem = new Apply(member);
            redisClient.hset(MEMBER_ADD_APPLY_KEY, phone, JSON.toJSONString(applyMem));
            redisClient.expire(MEMBER_ADD_APPLY_KEY, MEMBER_ADD_APPLY_EXPIRE);
            return EncodeUtils.encode("请获取管理员同意");
        } else {
            Apply applyMem = JSON.parseObject(apply, Apply.class);
            if (!applyMem.isAgree()) {
                return EncodeUtils.encode("请获取管理员同意");
            }
        }
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("photo");
        String relativePath = "static/img/welcome/";
        if (CollectionUtils.isNotEmpty(files)) {
            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) {
                    continue;
                }
                String photoType = getFileType(file.getOriginalFilename());
                String fileName = member.getMobile() + photoType;
                String fileUrl = relativePath + fileName;
                if (StringUtils.isBlank(member.getFirstImgUrl())){
                    member.setFirstImgUrl(fileUrl);
                }
                String dirPath = getBasePath() + relativePath;
                FileUploadUtils.uploadFile(file, dirPath, fileName);
            }
            if(StringUtils.isNotBlank(member.getMobile())) {
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
                EncodeUtils.encodeObj(member);
                member.setShowMobile(hideNum(member.getMobile(), 3, 7));
                res.add(member);
            }
        });
        res.sort(Comparator.comparing(Member::getId).reversed());
        // 最多显示1个
        List<Member> subRes = res.subList(0, Math.min(1, res.size()));
        return JSON.toJSONString(subRes);
    }

    @GetMapping(value = "member/detail")
    public ModelAndView detail(@RequestParam(value = "phone") String phone) {
        String memberJson = redisClient.hget(MEMBER_LIST_KEY, phone);
        Member member = JSON.parseObject(memberJson, Member.class);
        ModelAndView mav = new ModelAndView();
        List<Img> imgList = new ArrayList<>();
        List<String> tagList = new ArrayList<>();
        if (member != null && CollectionUtils.isNotEmpty(member.getPhotos())) {
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

    @GetMapping(value = "member/uploadpage")
    public ModelAndView uploadPage() {
        ModelAndView mav = new ModelAndView();
        List<Member> members = redisClient.hgetAllValue(MEMBER_LIST_KEY, Member.class);
        mav.addObject("members", members);
        mav.setViewName("uploadimg");
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

    @GetMapping(value = "member/tag")
    public ModelAndView tag (){
        ModelAndView mav = new ModelAndView();
        List<Member> members = redisClient.hgetAllValue(MEMBER_LIST_KEY, Member.class);
        mav.addObject("members", members);
        mav.setViewName("memtags");
        return mav;
    }

    @PostMapping(value = "member/tagsave")
    public @ResponseBody String tagSave (@RequestParam(value = "phone") String phone, @RequestParam(value = "tags") String tags){
        if (StringUtils.isNotBlank(tags)) {
            tags = tags.replaceAll("\r\n", "|");
            redisClient.hset(MEMBER_TAGS_KEY, phone, tags);
        }
        return "OK";
    }
}
