package club.own.site.controller;

import club.own.site.bean.BlogItem;
import club.own.site.bean.Member;
import club.own.site.config.redis.RedisClient;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static club.own.site.constant.ProjectConstant.BLOG_LIST_KEY;
import static club.own.site.constant.ProjectConstant.MEMBER_LIST_KEY;
import static club.own.site.utils.DateTimeUtils.getCurrentFormatTime;
import static club.own.site.utils.FileUploadUtils.getBasePath;

@Slf4j
@Controller
public class BlogController extends BaseController {

    @Autowired
    private RedisClient redisClient;

    @GetMapping(value = "blog/list")
    public @ResponseBody String blogList (){
        Map<String, String> blogMap = redisClient.hgetAll(BLOG_LIST_KEY);
        List<String> values = Lists.newArrayList(blogMap.values());
        List<BlogItem> blogItems = Lists.newArrayList();
        values.forEach(s -> {
            if (StringUtils.isNotBlank(s)) {
                BlogItem blogItem = JSON.parseObject(s, BlogItem.class);
                blogItems.add(blogItem);
            }
        });
        blogItems.sort(Comparator.comparing(BlogItem::getId).reversed());
        // 最多显示4个
        List<BlogItem> subRes = blogItems.subList(0, Math.min(3, blogItems.size()));
        return JSON.toJSONString(subRes);
    }

    @PostMapping(value = "blog/add")
    public @ResponseBody String add (MultipartHttpServletRequest request){
        Map<String, String> params = parseRequestParam(request.getParameterMap());
        List<MultipartFile> files = request.getFiles("photoArr");
        BlogItem blogItem = new BlogItem();
        blogItem.setTitle(params.get("title"));
        blogItem.setBrief(params.get("brief"));
        blogItem.setContent(params.get("content"));
        String mobile = params.get("mobile");
        if (StringUtils.isNotBlank(mobile)) {
            String memberJson = redisClient.hget(MEMBER_LIST_KEY, mobile);
            Member member = JSON.parseObject(memberJson, Member.class);
            blogItem.setAuthor(member);
        }
        blogItem.setCreateTime(getCurrentFormatTime());
        String relativePath = "static/img/blog/" + blogItem.getId() + "/";
        if (CollectionUtils.isNotEmpty(files)) {
            for (int i=0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                if (file == null || file.isEmpty()) {
                    continue;
                }
                String desFileName = i + ".jpg";
                String fileUrl = relativePath + desFileName;
                if (!blogItem.getImgList().contains(fileUrl)) {
                    blogItem.getImgList().add(i, fileUrl);
                }
                if (StringUtils.isBlank(blogItem.getFirstImgUrl())){
                    blogItem.setFirstImgUrl(fileUrl);
                }
                String dirPath = getBasePath() + relativePath;
                FileUploadUtils.uploadFile(file, dirPath, desFileName);
            }
        }

        redisClient.hset(BLOG_LIST_KEY, String.valueOf(blogItem.getId()), JSON.toJSONString(blogItem));

        return "OK";
    }

}
