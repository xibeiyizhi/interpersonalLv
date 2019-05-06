package club.own.site.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class IndexController extends BaseController {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView index() throws Exception {
        if(log.isInfoEnabled()){
            log.info("index page...");
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        return mav;
    }
}
