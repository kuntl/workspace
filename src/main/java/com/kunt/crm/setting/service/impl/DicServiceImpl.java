package com.kunt.crm.setting.service.impl;

import com.kunt.crm.setting.dao.DicTypeDao;
import com.kunt.crm.setting.dao.DicValueDao;
import com.kunt.crm.setting.domain.DicType;
import com.kunt.crm.setting.domain.DicValue;
import com.kunt.crm.setting.service.DicService;
import com.kunt.crm.util.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    public Map<String, List<DicValue>> getAll() {
        Map<String,List<DicValue>> map = new HashMap<String,List<DicValue>>();
        //将字典类型列表取出
        List<DicType> dtList = dicTypeDao.getTypeList();

        //将字典类型列表遍历
        for (DicType dt:dtList){
            String code = dt.getCode();
            //根据每个字典类型取得字典值
            List<DicValue> dvList = dicValueDao.getListByCode(code);
            map.put(code+"List",dvList);
        }
        return map;
    }
}
