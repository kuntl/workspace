package com.kunt.crm.workbench.dao;

import com.kunt.crm.workbench.domain.Activity;
import com.kunt.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue c);

    int getTotalByCondition(Map<String, Object> map);

    List<Clue> getClueListByCondition(Map<String, Object> map);

    Clue getById(String id);

    int update(Clue c);

    int delete(String[] ids);

    Clue detail(String id);
}
