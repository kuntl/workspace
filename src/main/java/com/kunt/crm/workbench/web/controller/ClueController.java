package com.kunt.crm.workbench.web.controller;



import com.kunt.crm.setting.domain.User;
import com.kunt.crm.setting.service.UserService;
import com.kunt.crm.setting.service.impl.UserServiceImpl;
import com.kunt.crm.util.DateTimeUtil;
import com.kunt.crm.util.PrintJson;
import com.kunt.crm.util.ServiceFactory;
import com.kunt.crm.util.UUIDUtil;
import com.kunt.crm.vo.PaginationVO;
import com.kunt.crm.vo.ShowClueAndActivityRelationVO;
import com.kunt.crm.workbench.domain.*;
import com.kunt.crm.workbench.service.ActivityService;
import com.kunt.crm.workbench.service.ClueService;
import com.kunt.crm.workbench.service.impl.ActivityServiceImpl;
import com.kunt.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到市场线索控制器");
        String path = request.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if ("/workbench/clue/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/clue/pageList.do".equals(path)){
            pageList(request,response);
        }else if ("/workbench/clue/getUserListAndClue.do".equals(path)){
            getUserListAndClue(request,response);
        }else if ("/workbench/clue/update.do".equals(path)){
            update(request,response);
        }else if ("/workbench/clue/delete.do".equals(path)){
            delete(request,response);
        }else if ("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/clue/getRemarkListByCid.do".equals(path)){
            getRemarkListByCid(request,response);
        }else if ("/workbench/clue/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if ("/workbench/clue/updateRemark.do".equals(path)){
            updateRemark(request,response);
        }else if ("/workbench/clue/saveRemark.do".equals(path)){
            saveRemark(request,response);
        }else if ("/workbench/clue/showActivityAndClueRelationList.do".equals(path)){
            showActivityAndClueRelationList(request,response);
        }else if ("/workbench/clue/deleteRelation.do".equals(path)){
            deleteRelation(request,response);
        }else if ("/workbench/clue/bundRelation.do".equals(path)){
            bundRelation(request,response);
        }else if ("/workbench/clue/bundSave.do".equals(path)){
            bundSave(request,response);
        }
    }



    private void bundSave(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入保存关联关系操作");
        String cid = request.getParameter("cid");
        String[] aids = request.getParameterValues("aid");
        List<ClueActivityRelation> carList = new ArrayList<ClueActivityRelation>();
        for (int i = 0; i < aids.length; i++) {
            ClueActivityRelation tmp = new ClueActivityRelation();
            tmp.setId(UUIDUtil.getUUID());
            tmp.setActivityId(aids[i]);
            tmp.setClueId(cid);
            carList.add(tmp);
        }
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.bundSave(carList);
        PrintJson.printJsonFlag(response,flag);
    }

    private void bundRelation(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入关联关系操作");
        Map<String,Object> map = new HashMap<String,Object>();
        String a = request.getParameter("a");
        String clueId = request.getParameter("clueId");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);

        int skipCount = (pageNo-1)*pageSize;
        map.put("a",a);
        map.put("clueId",clueId);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        PaginationVO<Activity> r = cs.bundRelation(map);
        PrintJson.printJsonObj(response,r);

    }

    private void deleteRelation(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入删除关联关系操作");
        String carId = request.getParameter("carId");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.deleteRelation(carId);
        PrintJson.printJsonFlag(response,flag);
    }

    private void showActivityAndClueRelationList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行显示关联市场活动操作");
        Map<String,Object> map = new HashMap<String,Object>();
        String clueId = request.getParameter("clueId");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);

        int skipCount = (pageNo-1)*pageSize;
        map.put("clueId",clueId);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        PaginationVO<ShowClueAndActivityRelationVO> r = cs.showActivityAndClueRelationList(map);
        PrintJson.printJsonObj(response,r);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行添加备注操作");
        String activityId = request.getParameter("clueId");
        String noteContent = request.getParameter("noteContent");
        String id = UUIDUtil.getUUID();
        String creatTime = DateTimeUtil.getSysTime();
        String creatBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ClueRemark cr = new ClueRemark();
        cr.setId(id);
        cr.setNoteContent(noteContent);
        cr.setClueId(activityId);
        cr.setCreateBy(creatBy);
        cr.setCreateTime(creatTime);
        cr.setEditFlag(editFlag);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.saveRemark(cr);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("cr",cr);
        PrintJson.printJsonObj(response,map);
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行修改备注操作");
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)(request.getSession().getAttribute("user"))).getName();
        String editFlag = "1";
        ClueRemark cr = new ClueRemark();
        cr.setId(id);
        cr.setNoteContent(noteContent);
        cr.setEditTime(editTime);
        cr.setEditBy(editBy);
        cr.setEditFlag(editFlag);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.updateRemark(cr);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("cr",cr);
        PrintJson.printJsonObj(response,map);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("删除备注操作");
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemarkListByCid(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据线索id，取得备注信息列表");
        String clueId = request.getParameter("clueId");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<ClueRemark> arList = cs.getRemarkListByCid(clueId);
        PrintJson.printJsonObj(response,arList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转到详细信息页的操作");
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue c = cs.detail(id);
        request.setAttribute("c",c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索的删除操作");
        String ids[] = request.getParameterValues("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索修改操作");

        String id = request.getParameter("id");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");

        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");


        Clue c = new Clue();

        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setCompany(company);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setEditBy(editBy);
        c.setEditTime(editTime);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.update(c);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserListAndClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询用户信息列表和根据线索id查询单条记录操作");
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Map<String,Object> map = cs.getUserListAndClue(id);
        PrintJson.printJsonObj(response,map);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询线索信息列表的操作（结合条件查询+分页查询）");

        String fullname = request.getParameter("fullname");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String source = request.getParameter("source");
        String owner = request.getParameter("owner");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");




        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);

        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);


        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        PaginationVO<Clue> vo = cs.pageList(map);
        PrintJson.printJsonObj(response,vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索添加操作");
        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setCompany(company);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response,flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        PrintJson.printJsonObj(response,uList);
    }


}
