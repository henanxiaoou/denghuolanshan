package top.denghuolanshan.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import top.denghuolanshan.model.SysUser;
import top.denghuolanshan.service.SysUserService;
import top.denghuolanshan.util.MD5Util;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName UserController
 * @Description 做页面跳转
 * @Author 小欧
 * @Date 2019/6/3 21:21
 * @Version 1.0
 **/
@Controller
public class UserController {
    @Resource
    private SysUserService sysUserService;

    @RequestMapping("/logout.page")
    public void logout(HttpServletRequest request,HttpServletResponse response) throws IOException {
        // 从session中移除当前已经登陆的session
        request.getSession().invalidate();
        String path = "signin.jsp";
        response.sendRedirect(path);
    }

    @RequestMapping("/login.page")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        SysUser sysUser = sysUserService.findByKeyword(username);
        String errorMsg = "";
        // 因为某种情况下是用户执行某个请求因为，没有权限(需要登陆才能使用)跳转到登陆页面，那么用户登陆成功后要跳转到用户之前执行的页面
        String ret = request.getParameter("ret");
        if (StringUtils.isBlank(username)) {
            errorMsg = "用户名不能为空";
        }else if (StringUtils.isBlank(password)){
            errorMsg = "密码不能为空";
        }else if (sysUser == null){
            errorMsg = "该用户不存在";
        }else if (!sysUser.getPassword().equals(MD5Util.encrypt(password))){
            errorMsg = "用户名或密码错误";
        }else if (sysUser.getStatus() != 1){
            errorMsg = "用户已被冻结请联系管理员";
        }else {
            // login success
            request.getSession().setAttribute("user",sysUser);
            if (StringUtils.isNotBlank(ret)) {
                // 当用户登陆之前有请求时登陆成功后要继续执行用户登陆之前的请求
                response.sendRedirect(ret);
            }else {
                response.sendRedirect("/admin/index.page");
            }
        }
        request.setAttribute("error",errorMsg);
        request.setAttribute("username",username);
        if (StringUtils.isNotBlank(ret)) {
            request.setAttribute("ret",ret);
        }
        String path = "signin.jsp";
        request.getRequestDispatcher(path).forward(request, response);
    }
}
