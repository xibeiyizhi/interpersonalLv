package club.own.site.controller;

import club.own.site.bean.BlogItem;
import club.own.site.bean.Member;
import club.own.site.bean.ProdItem;
import club.own.site.config.redis.RedisClient;
import club.own.site.enums.BlogCategoryEnum;
import club.own.site.enums.ProductionTypeEnum;
import club.own.site.utils.DateTimeUtils;
import club.own.site.utils.TextUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

import static club.own.site.constant.ProjectConstant.*;
import static club.own.site.enums.BlogCategoryEnum.getNameByCode;

@Slf4j
@Controller
public class IndexController extends BaseController {


    @Autowired
    private RedisClient redisClient;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView root() throws Exception {
        List<ProdItem> prodItems = getProdItems();
        ModelAndView mav = new ModelAndView();
        mav.addObject("version", System.currentTimeMillis());
        mav.addObject("productionTypes", ProductionTypeEnum.values());
        mav.addObject("prodList", prodItems);
        mav.setViewName("index");
        return mav;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("version", System.currentTimeMillis());
        mav.addObject("productionTypes", ProductionTypeEnum.values());
        mav.addObject("prodList",  getProdItems());
        mav.setViewName("index");
        return mav;
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public ModelAndView service() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("services");
        return mav;
    }

    @RequestMapping(value = "/blog", method = RequestMethod.GET)
    public ModelAndView blog(@RequestParam(name = "position", required = false, defaultValue = "") String position,
                             @RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNum,
                             @RequestParam(name = "cate", required = false, defaultValue = "-1") String cateCode) throws Exception {
        String blogListKey = Integer.valueOf(cateCode) < 0 ? BLOG_LIST_KEY : (BLOG_CATE_LIST_KEY + getNameByCode(cateCode));
        long total = redisClient.llen(blogListKey);
        int pageSize = 5, paginationSize = 5;
        int totalPageCount = Long.valueOf(total / pageSize + (total % pageSize > 0 ? 1 : 0)).intValue();
        List<Integer> showPageNums = Lists.newArrayList();
        int paginationStart = 1, paginationEnd = totalPageCount;
        if (pageNum + 2 <= totalPageCount && pageNum > 2) {
            paginationStart = pageNum - 2;
            paginationEnd = pageNum + 2;
        } else if (pageNum <= 2) {
            paginationEnd = Math.min(paginationSize, totalPageCount);
        } else {
            paginationStart = Math.max(totalPageCount - paginationSize, 0);
        }
        for (int i= paginationStart; i <= paginationEnd; i++) {
            showPageNums.add(i);
        }
        boolean showNext = totalPageCount > paginationSize;
        int start = pageNum - 1 > 0 ? ((pageNum - 1) * pageSize) : 0;
        List<String> blogList = redisClient.sort(blogListKey, "", start, pageSize, false);
        List<BlogItem> blogItems = Lists.newArrayList();
        blogList.stream().filter(Objects::nonNull).forEach(id -> {
            if (StringUtils.isNotBlank(id)) {
                String blogContent = redisClient.hget(BLOG_ITEM_KEY + id, BLOG_BODY_KEY);
                if (StringUtils.isNotBlank(blogContent)) {
                    BlogItem blogItem = JSON.parseObject(blogContent, BlogItem.class);
                    blogItem.setCreateTime(DateTimeUtils.toBlogShowFormat(blogItem.getCreateTime()));
                    blogItems.add(blogItem);
                } else {
                    // 脏数据，删除
                    if (Integer.valueOf(cateCode) >= 0) {
                        redisClient.lrem(BLOG_CATE_LIST_KEY + getNameByCode(cateCode), -1, id);
                    }
                }
            }
        });
        ModelAndView mav = new ModelAndView();
        mav.setViewName("blog");
        mav.addObject("pageNums", showPageNums);
        mav.addObject("showNext", showNext);
        mav.addObject("curr", pageNum);
        mav.addObject("next", pageNum + 1);
        mav.addObject("blogItems", blogItems);
        mav.addObject("blogCates", BlogCategoryEnum.getBlogCategories());
        mav.addObject("currCate", cateCode);
        mav.addObject("position", position);
        return mav;
    }

    @RequestMapping(value = "/singlepost", method = RequestMethod.GET)
    public ModelAndView singlePost(@RequestParam(value = "id", required = false) String id) throws Exception {
        ModelAndView mav = new ModelAndView();
        if (StringUtils.isBlank(id)) {
            long len = redisClient.llen(BLOG_LIST_KEY);
            id = redisClient.lindex(BLOG_LIST_KEY, Long.valueOf(len - 1).intValue());
        }
        String blogItemJson = redisClient.hget(BLOG_ITEM_KEY + id, BLOG_BODY_KEY);
        if (StringUtils.isNotBlank(blogItemJson)) {
            BlogItem blogItem = JSON.parseObject(blogItemJson, BlogItem.class);
            // 更新浏览数
            blogItem.setViewCount(blogItem.getViewCount() + 1);
            redisClient.hset(BLOG_ITEM_KEY + id, BLOG_BODY_KEY, JSON.toJSONString(blogItem));
            mav.addObject("blogItem", blogItem);
            mav.addObject("blogBodyRichText", TextUtils.getParagraphList(blogItem.getContent()));
        }
        String category = redisClient.hget(BLOG_ITEM_KEY + id, BLOG_CATE_KEY);
        if (StringUtils.isNotBlank(category)) {
            String cateName = BlogCategoryEnum.getNameByCode(category);
            mav.addObject("blogCateCode", category);
            mav.addObject("blogCate", cateName);
        }
        mav.addObject("blogCates", BlogCategoryEnum.getBlogCategories());

        List<Member> members = redisClient.hgetAllValue(MEMBER_LIST_KEY, Member.class);
        mav.addObject("members", members);
        mav.setViewName("single-post");
        return mav;
    }

    @RequestMapping(value = "/stat", method = RequestMethod.GET)
    public ModelAndView statistics() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("statistics");
        return mav;
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public ModelAndView welcome() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("welcome");
        return mav;
    }

    @RequestMapping(value = "/lab", method = RequestMethod.GET)
    public ModelAndView lab() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("lab");
        return mav;
    }

    @RequestMapping(value = "/future", method = RequestMethod.GET)
    public ModelAndView future() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("future");
        return mav;
    }
}
