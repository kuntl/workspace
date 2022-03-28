package com.kunt.crm.setting.dao;

import com.kunt.crm.setting.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    User login(Map<String, String> map);

    List<User> getUserList();
}
