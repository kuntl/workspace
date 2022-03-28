package com.kunt.crm.workbench.service.impl;

import com.kunt.crm.setting.dao.UserDao;
import com.kunt.crm.setting.domain.User;
import com.kunt.crm.util.SqlSessionUtil;
import com.kunt.crm.vo.PaginationVO;
import com.kunt.crm.vo.ShowClueAndActivityRelationVO;
import com.kunt.crm.workbench.dao.*;
import com.kunt.crm.workbench.domain.Activity;
import com.kunt.crm.workbench.domain.Clue;
import com.kunt.crm.workbench.domain.ClueActivityRelation;
import com.kunt.crm.workbench.domain.ClueRemark;
import com.kunt.crm.workbench.service.ClueService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    public boolean save(Clue c) {
        boolean flag = true;
        int count = clueDao.save(c);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    public PaginationVO<Clue> pageList(Map<String, Object> map) {
        //取得total
        int total = clueDao.getTotalByCondition(map);

        //取得dataList
        List<Clue> dataList = clueDao.getClueListByCondition(map);

        //将total和dataList封装到vo中
        PaginationVO<Clue> vo = new PaginationVO<Clue>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回
        return vo;
    }

    public Map<String, Object> getUserListAndClue(String id) {
        //取uList
        List<User> uList = userDao.getUserList();
        //取c
        Clue c = clueDao.getById(id);

        //将uList和a打包到map中
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uList",uList);
        map.put("c",c);

        //返回map就可以了
        return map;
    }

    public boolean update(Clue c) {
        boolean flag = true;
        int count = clueDao.update(c);
        if (count!=1){
            flag = false;
        }
        return flag;
    }

    public boolean delete(String[] ids) {
        boolean flag = true;
        //查询出需要删除的备注的数量
        int count1 = clueRemarkDao.getCountByAids(ids);

        //删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = clueRemarkDao.deleteByAids(ids);

        if (count1 != count2){
            flag = false;
        }
        //删除市场活动
        int count3 = clueDao.delete(ids);
        if (count3 != ids.length){
            flag = false;
        }
        return flag;
    }

    public Clue detail(String id) {
        Clue c = clueDao.detail(id);
        return c;
    }

    public List<ClueRemark> getRemarkListByCid(String cId) {
        List<ClueRemark> crList = clueRemarkDao.getRemarkListByCid(cId);
        return crList;
    }

    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = clueRemarkDao.deleteById(id);
        if (count != 1){
            flag = false;
        }

        return flag;
    }

    public boolean updateRemark(ClueRemark cr) {
        boolean flag = true;
        int count = clueRemarkDao.updateRemark(cr);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    public boolean saveRemark(ClueRemark cr) {
        boolean flag = true;
        int count = clueRemarkDao.saveRemark(cr);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    public PaginationVO<ShowClueAndActivityRelationVO> showActivityAndClueRelationList(Map<String,Object> map) {
        PaginationVO<ShowClueAndActivityRelationVO> r = new PaginationVO<ShowClueAndActivityRelationVO>();
        List<ShowClueAndActivityRelationVO> aList;
        aList = clueActivityRelationDao.showActivityAndClueRelationList(map);
        r.setDataList(aList);
        int total = clueActivityRelationDao.calculateTotal((String )map.get("clueId"));
        r.setTotal(total);
        return r;
    }



    public boolean deleteRelation(String carId) {
        boolean flag = true;
        int count = clueActivityRelationDao.deleteRelation(carId);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    public PaginationVO<Activity> bundRelation(Map<String,Object> map) {
        PaginationVO<Activity> r = new PaginationVO<Activity>();
        List<Activity> aList = activityDao.bundRelation(map);
        int total = activityDao.calculateTotal(map);
        r.setTotal(total);
        r.setDataList(aList);
        return r;
    }

    public boolean bundSave(List<ClueActivityRelation> carList) {
        boolean flag = true;
        for (ClueActivityRelation car:carList) {
            int count = clueActivityRelationDao.bundSave(car);
            if (count != 1){
                flag = false;
            }
        }
        return flag;
    }




}
