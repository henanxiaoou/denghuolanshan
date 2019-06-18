package top.denghuolanshan.filter;


import lombok.extern.slf4j.Slf4j;
import top.denghuolanshan.common.RequestHolder;
import top.denghuolanshan.model.SysUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName LoginFilter
 * @Description 校验用户是否登陆
 * @Author 小欧
 * @Date 2019/6/5 13:56
 * @Version 1.0
 **/
@Slf4j
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        SysUser sysUser = (SysUser) request.getSession().getAttribute("user");
        if (sysUser == null){
            // 用户还没有登陆，跳转到登陆页面
            String path = "/signin.jsp";
            response.sendRedirect(path);
            return;
        }
        // 如果用户存在
        RequestHolder.add(sysUser);
        RequestHolder.add(request);
        filterChain.doFilter(servletRequest,servletResponse);
        return;
    }

    @Override
    public void destroy() {

    }
}
