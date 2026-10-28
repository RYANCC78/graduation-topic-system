package com.buptsse.topic.dao.impl;

import com.buptsse.topic.dao.TopicDao;
import com.buptsse.topic.model.Topic;
import com.buptsse.topic.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TopicDaoImpl implements TopicDao {

    private Topic map(ResultSet rs) throws SQLException {
        Topic t = new Topic();
        t.setTopicId(rs.getInt("topic_id"));
        t.setTitle(rs.getString("title"));
        t.setDescription(rs.getString("description"));
        t.setRequiredAbility(rs.getString("required_ability"));
        t.setQuota(rs.getInt("quota"));
        t.setRemainingQuota(rs.getInt("remaining_quota"));
        t.setTeacherId(rs.getInt("teacher_id"));
        try { t.setTeacherName(rs.getString("teacher_name")); } catch (SQLException ignored) {}
        t.setStatus(rs.getString("status"));
        t.setCreateTime(rs.getTimestamp("create_time"));
        return t;
    }

    @Override
    public List<Topic> findPublished(String keyword) {
        List<Topic> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT t.*, u.name AS teacher_name FROM topic t JOIN user u ON t.teacher_id=u.user_id WHERE t.status='published'");
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND t.title LIKE '%").append(keyword).append("%'");
        }
        sql.append(" ORDER BY t.create_time DESC");
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
    public List<Topic> findByTeacherId(Integer teacherId) {
        List<Topic> list = new ArrayList<>();
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("SELECT * FROM topic WHERE teacher_id=? ORDER BY create_time DESC");
            ps.setInt(1, teacherId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps, rs); }
        return list;
    }

    @Override
    public List<Topic> findAll() {
        List<Topic> list = new ArrayList<>();
        Connection conn = null; Statement st = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT t.*, u.name AS teacher_name FROM topic t JOIN user u ON t.teacher_id=u.user_id ORDER BY t.create_time DESC");
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, st, rs); }
        return list;
    }

    @Override
    public Topic findById(Integer topicId) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("SELECT * FROM topic WHERE topic_id=?");
            ps.setInt(1, topicId);
            rs = ps.executeQuery();
            if (rs.next()) return map(rs);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps, rs); }
    }

    @Override
    public void insert(Topic t) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("INSERT INTO topic(title,description,required_ability,quota,remaining_quota,teacher_id,status,create_time) VALUES(?,?,?,?,?,?,?,NOW())");
            ps.setString(1, t.getTitle());
            ps.setString(2, t.getDescription());
            ps.setString(3, t.getRequiredAbility());
            ps.setInt(4, t.getQuota());
            ps.setInt(5, t.getQuota());
            ps.setInt(6, t.getTeacherId());
            ps.setString(7, "published");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps); }
    }

    @Override
    public void update(Topic t) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("UPDATE topic SET title=?, description=?, required_ability=? WHERE topic_id=? AND teacher_id=?");
            ps.setString(1, t.getTitle());
            ps.setString(2, t.getDescription());
            ps.setString(3, t.getRequiredAbility());
            ps.setInt(4, t.getTopicId());
            ps.setInt(5, t.getTeacherId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps); }
    }

    @Override
    public void updateStatus(Integer topicId, String status) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("UPDATE topic SET status=? WHERE topic_id=?");
            ps.setString(1, status);
            ps.setInt(2, topicId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps); }
    }

    @Override
    public void decreaseQuota(Integer topicId) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("UPDATE topic SET remaining_quota = remaining_quota - 1 WHERE topic_id=? AND remaining_quota > 0");
            ps.setInt(1, topicId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps); }
    }

    @Override
    public void increaseQuota(Integer topicId) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("UPDATE topic SET remaining_quota = remaining_quota + 1 WHERE topic_id=?");
            ps.setInt(1, topicId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps); }
    }
}
