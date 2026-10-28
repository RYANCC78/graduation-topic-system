package com.buptsse.topic.dao;

import com.buptsse.topic.model.User;
import java.util.List;

public interface UserDao {
    User findByAccount(String account);
    User findById(Integer userId);
    List<User> findAll(String roleFilter, String keyword);
    void insert(User user);
    void update(User user);
    void updateStatus(Integer userId, Integer status);
}
