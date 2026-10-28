package com.buptsse.topic.service;

import com.buptsse.topic.dao.UserDao;
import com.buptsse.topic.dao.impl.UserDaoImpl;
import com.buptsse.topic.model.User;
import com.buptsse.topic.util.PasswordUtil;

public class LoginService {
    private UserDao userDao = new UserDaoImpl();

    public User login(String account, String rawPassword) {
        if (account == null || account.trim().isEmpty()) {
            throw new IllegalArgumentException("请输入账号");
        }
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("请输入密码");
        }
        User u = userDao.findByAccount(account.trim());
        if (u == null) {
            throw new IllegalArgumentException("账号不存在");
        }
        if (u.getStatus() != null && u.getStatus() == 0) {
            throw new IllegalArgumentException("账号已被禁用，请联系管理员");
        }
        if (!u.getPassword().equals(PasswordUtil.encrypt(rawPassword))) {
            throw new IllegalArgumentException("密码错误");
        }
        return u;
    }

    public User findById(Integer userId) {
        return userDao.findById(userId);
    }
}
