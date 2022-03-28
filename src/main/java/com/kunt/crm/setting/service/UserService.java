package com.kunt.crm.setting.service;

import com.kunt.crm.exception.LoginException;
import com.kunt.crm.setting.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
