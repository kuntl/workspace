package com.kunt.crm.setting.web.controller;

import com.kunt.crm.setting.domain.User;
import com.kunt.crm.setting.service.UserService;
import com.kunt.crm.setting.service.impl.UserServiceImpl;
import com.kunt.crm.util.MD5Util;
import com.kunt.crm.util.PrintJson;
import com.kunt.crm.util.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到用户控制器");
        String path = request.getServletPath();
        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if ("/settings/user/xxx.do".equals(path)){

        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入登录操作");
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        loginPwd = MD5Util.getMD5(loginPwd);
        String ip = request.getRemoteAddr();
        System.out.println("==================ip"+ip);

        //未来业务层开发，统一使用代理类形态的接口对象 代理类可提交事务，普通实现类不提交事务
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try{
            User user = us.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            e.printStackTrace();
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }

    }
}
