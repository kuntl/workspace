package com.kunt.crm.workbench.service;

import com.kunt.crm.vo.PaginationVO;
import com.kunt.crm.vo.ShowClueAndActivityRelationVO;
import com.kunt.crm.workbench.domain.Activity;
import com.kunt.crm.workbench.domain.Clue;
import com.kunt.crm.workbench.domain.ClueActivityRelation;
import com.kunt.crm.workbench.domain.ClueRemark;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean save(Clue c);

    PaginationVO<Clue> pageList(Map<String, Object> map);

    Map<String, Object> getUserListAndClue(String id);

    boolean update(Clue c);

    boolean delete(String[] ids);

    Clue detail(String id);

    List<ClueRemark> getRemarkListByCid(String clueId);

    boolean deleteRemark(String id);

    boolean updateRemark(ClueRemark cr);

    boolean saveRemark(ClueRemark cr);

    PaginationVO<ShowClueAndActivityRelationVO> showActivityAndClueRelationList(Map<String,Object> map);

    boolean deleteRelation(String carId);

    PaginationVO<Activity> bundRelation(Map<String,Object> map);

    boolean bundSave(List<ClueActivityRelation> carList);



}
