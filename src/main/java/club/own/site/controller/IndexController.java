package club.own.site.controller;

import club.own.site.bean.Img;
import club.own.site.bean.Quotation;
import club.own.site.constant.ProjectConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class IndexController extends BaseController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView root() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() throws Exception {
        ModelAndView mav = new ModelAndView();
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
    public ModelAndView blog() throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("blog");
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
