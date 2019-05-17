package club.own.site.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/member")
public class MemberController extends BaseController {

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String add(){

        return "success";
    }
}
