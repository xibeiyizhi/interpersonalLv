package club.own.site.controller;

import club.own.site.enums.BlogCategoryEnum;
import club.own.site.bean.BlogItem;
import club.own.site.bean.Comment;
import club.own.site.bean.Member;
import club.own.site.config.redis.RedisClient;
import club.own.site.utils.EncodeUtils;
import club.own.site.utils.FileUploadUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import static club.own.site.enums.BlogCategoryEnum.getNameByCode;
import static club.own.site.constant.ProjectConstant.*;
import static club.own.site.utils.DateTimeUtils.getCurrentFormatTime;
import static club.own.site.utils.FileUploadUtils.getBasePath;

@Slf4j
@Controller
public class BlogController extends BaseController {

    @Autowired
    private RedisClient redisClient;

    @GetMapping(value = "blog/list")
    public @ResponseBody String blogList (@RequestParam(name = "cate", required = false, defaultValue = "-1") String cateCode){
        String blogListKey = Integer.valueOf(cateCode) < 0 ? BLOG_LIST_KEY : (BLOG_CATE_LIST_KEY + getNameByCode(cateCode));
        List<String> blogList = redisClient.sort(blogListKey, "", 0, 3, false);
        List<BlogItem> blogItems = Lists.newArrayList();
        blogList.forEach(id -> {
            if (StringUtils.isNotBlank(id)) {
                String blogContent = redisClient.hget(BLOG_ITEM_KEY + id, BLOG_BODY_KEY);
                if (StringUtils.isNotBlank(blogContent)) {
                    BlogItem blogItem = JSON.parseObject(blogContent, BlogItem.class);
                    EncodeUtils.encodeObj(blogItem);
                    blogItems.add(blogItem);
                }
            }
        });

        return JSON.toJSONString(blogItems);
    }

    @GetMapping(value = "blog/comments")
    public @ResponseBody String blogComments (@RequestParam(name = "cate", required = false, defaultValue = "-1") String cateCode){
        String blogListKey = Integer.valueOf(cateCode) < 0 ? BLOG_LIST_KEY : (BLOG_CATE_LIST_KEY + getNameByCode(cateCode));
        List<String> blogList = redisClient.sort(blogListKey, "", 0, 3, false);
        List<Map<String, Object>> comments = Lists.newArrayList();
        blogList.forEach(id -> {
            if (StringUtils.isNotBlank(id)) {
                String blogContent = redisClient.hget(BLOG_ITEM_KEY + id, BLOG_BODY_KEY);
                if (StringUtils.isBlank(blogContent)) {
                    return;
                }
                BlogItem blogItem = JSON.parseObject(blogContent, BlogItem.class);
                EncodeUtils.encodeObj(blogItem);
                if (CollectionUtils.isNotEmpty(blogItem.getComments())) {
                    for (Comment comment : blogItem.getComments()) {
                        Map<String, Object> map = Maps.newHashMap();
                        map.put("id", comment.getId());
                        map.put("title", blogItem.getTitle());
                        map.put("comment", comment);
                        map.put("blogId", blogItem.getId());
                        comments.add(map);
                    }
                }
            }
        });
        comments.sort((o1, o2) -> Long.valueOf((long)o2.get("id") - (long)o1.get("id")).intValue());

        return JSON.toJSONString(comments);
    }

    @PostMapping(value = "blog/comment/add")
    public @ResponseBody String addComment (@RequestParam(name = "id") String blogId,
                                            HttpServletRequest request){
        Map<String, String> params = parseRequestParam(request.getParameterMap());
        String commentBody = params.get("comment");
        if (StringUtils.isBlank(commentBody)) {
            return "Comment is blank";
        }
        String phone = StringUtils.isNotBlank(params.get("phone")) ? params.get("phone") : default_user_phone;
        String blogItemJson = redisClient.hget(BLOG_ITEM_KEY + blogId, BLOG_BODY_KEY);
        if (StringUtils.isNotBlank(blogItemJson)) {
            BlogItem blogItem = JSON.parseObject(blogItemJson, BlogItem.class);
            Comment comment = new Comment();
            comment.setContent(commentBody);
            comment.setCreateTime(getCurrentFormatTime());
            String memberJson = redisClient.hget(MEMBER_LIST_KEY, phone);
            if (StringUtils.isNotBlank(memberJson)) {
                Member member = JSON.parseObject(memberJson, Member.class);
                comment.setAuthor(member);
            }
            blogItem.getComments().add(comment);
            redisClient.hset(BLOG_ITEM_KEY + blogId, BLOG_BODY_KEY, JSON.toJSONString(blogItem));
        }
        return "OK";
    }

    @GetMapping(value = "blog/comment/del")
    public @ResponseBody String delComment (@RequestParam(name = "id") String blogId,
                                            HttpServletRequest request){

        String blogItemJson = redisClient.hget(BLOG_ITEM_KEY + blogId, BLOG_BODY_KEY);
        if (StringUtils.isNotBlank(blogItemJson)) {
            BlogItem blogItem = JSON.parseObject(blogItemJson, BlogItem.class);
            blogItem.getComments().clear();
            redisClient.hset(BLOG_ITEM_KEY + blogId, BLOG_BODY_KEY, JSON.toJSONString(blogItem));
        }
        return "OK";
    }

    @GetMapping(value = "blog/del")
    public @ResponseBody String blogDel (@RequestParam(value = "idx", required = false) String idx){
        if (StringUtils.isNotBlank(idx)) {
            String id = redisClient.lindex(BLOG_LIST_KEY, Integer.valueOf(idx));
            if (StringUtils.isBlank(id)) {
                return "idx is not exist";
            }
            redisClient.lrem(BLOG_LIST_KEY, -1, id);
            String category = redisClient.hget(BLOG_ITEM_KEY + id, BLOG_CATE_KEY);
            redisClient.lrem(BLOG_CATE_LIST_KEY + getNameByCode(category), -1, id);
            redisClient.del(BLOG_ITEM_KEY + id);
        } else {
            String id = redisClient.lpop(BLOG_LIST_KEY);
            if (StringUtils.isBlank(id)) {
                return "ALL BLOG IS DELETED";
            }
            redisClient.lrem(BLOG_LIST_KEY, -1, id);
            String category = redisClient.hget(BLOG_ITEM_KEY + id, BLOG_CATE_KEY);
            redisClient.lrem(BLOG_CATE_LIST_KEY + getNameByCode(category), -1, id);
            redisClient.del(BLOG_ITEM_KEY + id);
            return "BLOG id=[" + id +"] is deleted";
        }
        return "OK";
    }

    @PostMapping(value = "blog/add")
    public @ResponseBody String add (MultipartHttpServletRequest request){
        Map<String, String> params = parseRequestParam(request.getParameterMap());
        List<MultipartFile> files = request.getFiles("photo");
        BlogItem blogItem = new BlogItem();
        blogItem.setTitle(params.get("title"));
        blogItem.setBrief(params.get("brief"));
        blogItem.setContent(params.get("content"));
        String mobile = params.get("phone");
        String category = params.get("category");
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

        redisClient.rpush(BLOG_LIST_KEY, String.valueOf(blogItem.getId()));
        redisClient.rpush(BLOG_CATE_LIST_KEY + getNameByCode(category), String.valueOf(blogItem.getId()));
        redisClient.hset(BLOG_ITEM_KEY + blogItem.getId(), BLOG_CATE_KEY, category);
        redisClient.hset(BLOG_ITEM_KEY + blogItem.getId(), BLOG_BODY_KEY, JSON.toJSONString(blogItem));
        return "OK";
    }

    @GetMapping(value = "blog/addpage")
    public ModelAndView addPage (){
        ModelAndView mav = new ModelAndView();
        List<Member> members = redisClient.hgetAllValue(MEMBER_LIST_KEY, Member.class);
        mav.addObject("members", members);
        mav.addObject("blogCates", BlogCategoryEnum.getBlogCategories());
        mav.setViewName("blogadd");
        return mav;
    }

    @PostMapping(value = "blog/autoadd")
    public @ResponseBody String autoAdd (HttpServletRequest request){
        Map<String, String> params = parseRequestParam(request.getParameterMap());
        BlogItem blogItem = new BlogItem();
        blogItem.setTitle(params.get("title"));
        blogItem.setBrief("来源：" + params.get("brief"));
        blogItem.setContent(params.get("content"));

        String mobile = "13488788682";
        String category = "0";
        if (StringUtils.isNotBlank(mobile)) {
            String memberJson = redisClient.hget(MEMBER_LIST_KEY, mobile);
            Member member = JSON.parseObject(memberJson, Member.class);
            blogItem.setAuthor(member);
        }
        blogItem.setCreateTime(getCurrentFormatTime());
        blogItem.setFirstImgUrl(params.get("imgUrl"));

        Set<String> titles = Sets.newHashSet();
        long len = redisClient.llen(BLOG_LIST_KEY);
        int start = Long.valueOf(Math.max(0, len - 10)).intValue();
        for (int i = start; i< len; i++) {
            String blogId = redisClient.lindex(BLOG_LIST_KEY, i);
            String blogBody = redisClient.hget(BLOG_ITEM_KEY + blogId, BLOG_BODY_KEY);
            BlogItem preBlog = JSON.parseObject(blogBody, BlogItem.class);
            if (preBlog == null) {
                continue;
            }
            titles.add(preBlog.getTitle());
        }
        if (!titles.contains(blogItem.getTitle())) {
            redisClient.rpush(BLOG_LIST_KEY, String.valueOf(blogItem.getId()));
            redisClient.rpush(BLOG_CATE_LIST_KEY + getNameByCode(category), String.valueOf(blogItem.getId()));
            redisClient.hset(BLOG_ITEM_KEY + blogItem.getId(), BLOG_BODY_KEY, JSON.toJSONString(blogItem));
            redisClient.hset(BLOG_ITEM_KEY + blogItem.getId(), BLOG_CATE_KEY, category);
        }

        return "OK";
    }

}
