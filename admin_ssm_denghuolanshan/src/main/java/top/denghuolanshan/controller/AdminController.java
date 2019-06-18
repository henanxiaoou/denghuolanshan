package top.denghuolanshan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName AdminController
 * @Description 系统请求跳转
 * @Author 小欧
 * @Date 2019/6/3 21:47
 * @Version 1.0
 **/
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/index.page")
    public ModelAndView index(){
        return new ModelAndView("admin");
    }
}
