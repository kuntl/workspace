package com.kunt.crm.workbench.dao;

import com.kunt.crm.vo.ShowClueAndActivityRelationVO;
import com.kunt.crm.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationDao {

    List<ShowClueAndActivityRelationVO> showActivityAndClueRelationList(Map<String,Object> map);

    int deleteRelation(String carId);

    int bundSave(ClueActivityRelation car);

    int calculateTotal(String clueId);
}
