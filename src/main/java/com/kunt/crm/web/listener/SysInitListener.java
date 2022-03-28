package com.kunt.crm.web.listener;

import com.kunt.crm.setting.domain.DicValue;
import com.kunt.crm.setting.service.DicService;
import com.kunt.crm.setting.service.impl.DicServiceImpl;
import com.kunt.crm.util.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {
    //该方法是用来监听上下文域对象的方法，当服务器启动，上下文域对象创建
    //对象创建完毕，马上执行该方法
    public void contextInitialized(ServletContextEvent event) {
        //System.out.println("上下文域对象创建了");
        System.out.println("服务器缓存处理数据字典开始");
        ServletContext application = event.getServletContext();
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map =ds.getAll();
        Set<String> set = map.keySet();
        for (String key:set){
            application.setAttribute(key,map.get(key));
        }
        System.out.println("服务器缓存处理数据字典结束");
    }

}
