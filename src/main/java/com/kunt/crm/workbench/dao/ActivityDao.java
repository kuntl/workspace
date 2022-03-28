package com.kunt.crm.workbench.dao;

import com.kunt.crm.vo.ShowClueAndActivityRelationVO;
import com.kunt.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity a);

    List<Activity> getActivityListByCondition(Map<String, Object> map);

    int getTotalByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Activity getById(String id);

    int update(Activity a);

    Activity detail(String id);

    List<Activity> bundRelation(Map<String,Object> map);

    int calculateTotal(Map<String,Object> map);


}
