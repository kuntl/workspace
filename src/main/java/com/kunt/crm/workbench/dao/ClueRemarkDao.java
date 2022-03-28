package com.kunt.crm.workbench.dao;

import com.kunt.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ClueRemark> getRemarkListByCid(String cId);

    int deleteById(String id);

    int updateRemark(ClueRemark cr);

    int saveRemark(ClueRemark cr);
}
