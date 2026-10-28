package com.buptsse.topic.service;

import com.buptsse.topic.dao.UserDao;
import com.buptsse.topic.dao.impl.UserDaoImpl;
import com.buptsse.topic.model.User;
import com.buptsse.topic.util.PasswordUtil;

import java.util.List;

public class AccountService {
    private UserDao userDao = new UserDaoImpl();

    public List<User> list(String roleFilter, String keyword) {
        return userDao.findAll(roleFilter, keyword);
    }

    public void add(String account, String rawPassword, String role, String name, String phone, String email) {
        if (account == null || account.trim().isEmpty()) throw new IllegalArgumentException("账号不能为空");
        if (rawPassword == null || rawPassword.length() < 6) throw new IllegalArgumentException("密码长度至少6位");
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("姓名不能为空");
        if (!"student".equals(role) && !"teacher".equals(role)) throw new IllegalArgumentException("角色只能是 student 或 teacher");
        if (userDao.findByAccount(account.trim()) != null) {
            throw new IllegalArgumentException("账号已存在");
        }
        User u = new User();
        u.setAccount(account.trim());
        u.setPassword(PasswordUtil.encrypt(rawPassword));
        u.setRole(role);
        u.setName(name.trim());
        u.setPhone(phone);
        u.setEmail(email);
        u.setStatus(1);
        userDao.insert(u);
    }

    public void edit(Integer userId, String name, String phone, String email) {
        User u = userDao.findById(userId);
        if (u == null) throw new IllegalArgumentException("用户不存在");
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("姓名不能为空");
        u.setName(name.trim());
        u.setPhone(phone);
        u.setEmail(email);
        userDao.update(u);
    }

    public void toggleStatus(Integer userId) {
        User u = userDao.findById(userId);
        if (u == null) throw new IllegalArgumentException("用户不存在");
        int newStatus = (u.getStatus() != null && u.getStatus() == 1) ? 0 : 1;
        userDao.updateStatus(userId, newStatus);
    }
}
