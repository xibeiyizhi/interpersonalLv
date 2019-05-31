package club.own.site.controller;

import club.own.site.bean.BlogItem;
import club.own.site.bean.Img;
import club.own.site.bean.Quotation;
import club.own.site.config.redis.RedisClient;
import club.own.site.constant.ProjectConstant;
import club.own.site.utils.DateTimeUtils;
import club.own.site.utils.EncodeUtils;
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

import java.util.*;

import static club.own.site.constant.ProjectConstant.*;

@Slf4j
@Controller
public class IndexController extends BaseController {


    @Autowired
    private RedisClient redisClient;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView root() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("version", System.currentTimeMillis());
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
    public ModelAndView blog(@RequestParam(name = "pageNum", required = false, defaultValue = "0") int pageNum) throws Exception {
        long total = redisClient.llen(BLOG_LIST_KEY);
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
            paginationStart = totalPageCount - paginationSize;
        }
        for (int i= paginationStart; i <= paginationEnd; i++) {
            showPageNums.add(i);
        }
        boolean showNext = totalPageCount > paginationSize;
        int start = pageNum - 1 > 0 ? ((pageNum - 1) * pageSize) : 0;
        List<String> blogList = redisClient.sort(BLOG_LIST_KEY, "", start, pageSize, false);
        List<BlogItem> blogItems = Lists.newArrayList();
        blogList.forEach(s -> {
            if (StringUtils.isNotBlank(s)) {
                BlogItem blogItem = JSON.parseObject(s, BlogItem.class);
                blogItem.setCreateTime(DateTimeUtils.toBlogShowFormat(blogItem.getCreateTime()));
                blogItems.add(blogItem);
            }
        });
        ModelAndView mav = new ModelAndView();
        mav.setViewName("blog");
        mav.addObject("pageNums", showPageNums);
        mav.addObject("showNext", showNext);
        mav.addObject("curr", pageNum);
        mav.addObject("next", pageNum + 1);
        mav.addObject("blogItems", blogItems);
        return mav;
    }

    @RequestMapping(value = "/singlepost", method = RequestMethod.GET)
    public ModelAndView singlePost() throws Exception {
        ModelAndView mav = new ModelAndView();
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
