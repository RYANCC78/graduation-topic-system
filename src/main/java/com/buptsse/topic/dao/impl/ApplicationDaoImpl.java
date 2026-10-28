package com.buptsse.topic.dao.impl;

import com.buptsse.topic.dao.ApplicationDao;
import com.buptsse.topic.model.Application;
import com.buptsse.topic.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDaoImpl implements ApplicationDao {

    private Application map(ResultSet rs) throws SQLException {
        Application a = new Application();
        a.setApplicationId(rs.getInt("application_id"));
        a.setStudentId(rs.getInt("student_id"));
        a.setTopicId(rs.getInt("topic_id"));
        a.setStatus(rs.getString("status"));
        a.setRejectReason(rs.getString("reject_reason"));
        a.setApplyTime(rs.getTimestamp("apply_time"));
        a.setReviewTime(rs.getTimestamp("review_time"));
        try { a.setStudentName(rs.getString("student_name")); } catch (SQLException ignored) {}
        try { a.setTopicTitle(rs.getString("topic_title")); } catch (SQLException ignored) {}
        try { a.setTeacherName(rs.getString("teacher_name")); } catch (SQLException ignored) {}
        return a;
    }

    @Override
    public Application findPendingByStudentId(Integer studentId) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("SELECT * FROM application WHERE student_id=? AND status='pending'");
            ps.setInt(1, studentId);
            rs = ps.executeQuery();
            if (rs.next()) return map(rs);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps, rs); }
    }

    @Override
    public Application findApprovedByStudentId(Integer studentId) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("SELECT * FROM application WHERE student_id=? AND status='approved'");
            ps.setInt(1, studentId);
            rs = ps.executeQuery();
            if (rs.next()) return map(rs);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps, rs); }
    }

    @Override
    public List<Application> findByStudentId(Integer studentId) {
        List<Application> list = new ArrayList<>();
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(
                "SELECT a.*, t.title AS topic_title, u.name AS teacher_name " +
                "FROM application a JOIN topic t ON a.topic_id=t.topic_id JOIN user u ON t.teacher_id=u.user_id " +
                "WHERE a.student_id=? ORDER BY a.apply_time DESC");
            ps.setInt(1, studentId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps, rs); }
        return list;
    }

    @Override
    public List<Application> findByTeacherId(Integer teacherId) {
        List<Application> list = new ArrayList<>();
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(
                "SELECT a.*, t.title AS topic_title, s.name AS student_name " +
                "FROM application a JOIN topic t ON a.topic_id=t.topic_id JOIN user s ON a.student_id=s.user_id " +
                "WHERE t.teacher_id=? ORDER BY a.apply_time DESC");
            ps.setInt(1, teacherId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps, rs); }
        return list;
    }

    @Override
    public Application findById(Integer applicationId) {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("SELECT * FROM application WHERE application_id=?");
            ps.setInt(1, applicationId);
            rs = ps.executeQuery();
            if (rs.next()) return map(rs);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps, rs); }
    }

    @Override
    public List<Application> findAllJoined() {
        List<Application> list = new ArrayList<>();
        Connection conn = null; Statement st = null; ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(
                "SELECT a.*, t.title AS topic_title, s.name AS student_name, u.name AS teacher_name " +
                "FROM application a JOIN topic t ON a.topic_id=t.topic_id " +
                "JOIN user s ON a.student_id=s.user_id " +
                "JOIN user u ON t.teacher_id=u.user_id ORDER BY a.apply_time DESC");
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, st, rs); }
        return list;
    }

    @Override
    public void insert(Application a) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("INSERT INTO application(student_id,topic_id,status,reject_reason,apply_time,review_time) VALUES(?,?,?,NULL,NOW(),NULL)");
            ps.setInt(1, a.getStudentId());
            ps.setInt(2, a.getTopicId());
            ps.setString(3, "pending");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps); }
    }

    @Override
    public void updateStatus(Integer applicationId, String status, String rejectReason) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("UPDATE application SET status=?, reject_reason=?, review_time=NOW() WHERE application_id=?");
            ps.setString(1, status);
            ps.setString(2, rejectReason);
            ps.setInt(3, applicationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps); }
    }

    @Override
    public void invalidatePendingByTopicId(Integer topicId) {
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement("UPDATE application SET status='rejected', reject_reason='课题已被教师撤销，申请自动失效', review_time=NOW() WHERE topic_id=? AND status='pending'");
            ps.setInt(1, topicId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally { DBUtil.close(conn, ps); }
    }
}
