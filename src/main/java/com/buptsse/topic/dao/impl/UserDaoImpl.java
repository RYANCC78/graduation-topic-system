package com.buptsse.topic.dao.impl;

import com.buptsse.topic.dao.UserDao;
import com.buptsse.topic.model.User;
import com.buptsse.topic.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setAccount(rs.getString("account"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        u.setName(rs.getString("name"));
        u.setPhone(rs.getString("phone"));
        u.setEmail(rs.getString("email"));
        u.setStatus(rs.getInt("status"));
        u.setCreateTime(rs.getTimestamp("create_time"));
        return u;
    }

    @Override
    public User findByAccount(String account) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("SELECT * FROM user WHERE account = ?");
            ps.setString(1, account);
            rs = ps.executeQuery();
            if (rs.next()) return map(rs);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps, rs); }
    }

    @Override
    public User findById(Integer userId) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("SELECT * FROM user WHERE user_id = ?");
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) return map(rs);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps, rs); }
    }

    @Override
    public List<User> findAll(String roleFilter, String keyword) {
        List<User> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM user WHERE role <> 'admin'");
        if (roleFilter != null && !roleFilter.isEmpty()) {
            sql.append(" AND role = '").append(roleFilter).append("'");
        }
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (account LIKE '%").append(keyword).append("%' OR name LIKE '%").append(keyword).append("%')");
        }
        sql.append(" ORDER BY user_id");
        Connection conn = null; Statement st = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(sql.toString());
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, st, rs); }
        return list;
    }

    @Override
    public void insert(User u) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("INSERT INTO user(account,password,role,name,phone,email,status,create_time) VALUES(?,?,?,?,?,?,?,NOW())");
            ps.setString(1, u.getAccount());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getRole());
            ps.setString(4, u.getName());
            ps.setString(5, u.getPhone());
            ps.setString(6, u.getEmail());
            ps.setInt(7, u.getStatus() == null ? 1 : u.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps); }
    }

    @Override
    public void update(User u) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("UPDATE user SET name=?, phone=?, email=? WHERE user_id=?");
            ps.setString(1, u.getName());
            ps.setString(2, u.getPhone());
            ps.setString(3, u.getEmail());
            ps.setInt(4, u.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps); }
    }

    @Override
    public void updateStatus(Integer userId, Integer status) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("UPDATE user SET status=? WHERE user_id=?");
            ps.setInt(1, status);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps); }
    }
}
