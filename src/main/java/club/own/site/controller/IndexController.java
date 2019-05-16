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

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ModelAndView detail(@RequestParam(name = "id") String name) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", name.toUpperCase());
        List<Img> imgList = new ArrayList<>();
        List<String> tagList = new ArrayList<>();
        Quotation quotation = new Quotation();
        if (name.toUpperCase().equals(ProjectConstant.MemberEnum.FATHER.name())) {
            for (int i=1; i<=10; i++) {
                imgList.add(new Img("father"+i, "pic"+i));
            }
            tagList.addAll(quotation.getFatherSay());
        } else if (name.toUpperCase().equals(ProjectConstant.MemberEnum.MOTHER.name())) {
            for (int i=1; i<=10; i++) {
                imgList.add(new Img("mother"+i, "pic"+i));
            }
            tagList.addAll(quotation.getMonSay());
        } else if (name.toUpperCase().equals(ProjectConstant.MemberEnum.DAUGHTER.name())) {
            for (int i=1; i<=10; i++) {
                imgList.add(new Img("daughter"+i, "pic"+i));
            }
            tagList.addAll(quotation.getSisSay());
        } else if (name.toUpperCase().equals(ProjectConstant.MemberEnum.SON.name())) {
            for (int i=1; i<=10; i++) {
                imgList.add(new Img("son"+i, "pic"+i));
            }
            tagList.addAll(quotation.getiSay());
        }
        mav.addObject("imgList", imgList);
        mav.addObject("tagList", tagList);
        mav.setViewName("detail");
        return mav;
    }
}
