package com.kunt.crm.web.filter;

import com.kunt.crm.setting.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入到验证有没有登录过的过滤器");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path = request.getServletPath();

        //不应该拦截的资源，自动放行请求
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            filterChain.doFilter(req,resp);
        //其他资源必须验证有没有登录过
        }else {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            //如果user不为null，说明登录过
            if (user!=null){
                filterChain.doFilter(req,resp);
                //没有登录过
            }else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }
}
