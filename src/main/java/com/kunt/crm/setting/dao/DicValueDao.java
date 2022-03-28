package com.kunt.crm.setting.dao;

import com.kunt.crm.setting.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
