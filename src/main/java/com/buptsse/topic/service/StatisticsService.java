package com.buptsse.topic.service;

import com.buptsse.topic.dao.ApplicationDao;
import com.buptsse.topic.dao.TopicDao;
import com.buptsse.topic.dao.UserDao;
import com.buptsse.topic.dao.impl.ApplicationDaoImpl;
import com.buptsse.topic.dao.impl.TopicDaoImpl;
import com.buptsse.topic.dao.impl.UserDaoImpl;
import com.buptsse.topic.model.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsService {
    private ApplicationDao applicationDao = new ApplicationDaoImpl();
    private TopicDao topicDao = new TopicDaoImpl();
    private UserDao userDao = new UserDaoImpl();

    public Map<String, Object> overview() {
        Map<String, Object> m = new HashMap<>();
        List<Application> all = applicationDao.findAllJoined();
        long pending = all.stream().filter(a -> "pending".equals(a.getStatus())).count();
        long approved = all.stream().filter(a -> "approved".equals(a.getStatus())).count();
        long rejected = all.stream().filter(a -> "rejected".equals(a.getStatus())).count();
        long cancelled = all.stream().filter(a -> "cancelled".equals(a.getStatus())).count();
        m.put("totalApps", all.size());
        m.put("pending", pending);
        m.put("approved", approved);
        m.put("rejected", rejected);
        m.put("cancelled", cancelled);
        m.put("topicCount", topicDao.findAll().size());
        m.put("studentCount", userDao.findAll("student", null).size());
        m.put("teacherCount", userDao.findAll("teacher", null).size());
        return m;
    }

    public List<Application> allApplications() {
        return applicationDao.findAllJoined();
    }

    public String[] exportHeaders() {
        return new String[]{"申请编号", "学生姓名", "课题名称", "指导教师", "申请状态", "驳回理由", "申请时间", "审核时间"};
    }

    public List<String[]> exportRows() {
        List<Application> all = applicationDao.findAllJoined();
        List<String[]> rows = new ArrayList<>();
        Map<String, String> statusText = new HashMap<>();
        statusText.put("pending", "待审核");
        statusText.put("approved", "已通过");
        statusText.put("rejected", "已驳回");
        statusText.put("cancelled", "已撤销");
        for (Application a : all) {
            rows.add(new String[]{
                String.valueOf(a.getApplicationId()),
                a.getStudentName(),
                a.getTopicTitle(),
                a.getTeacherName(),
                statusText.getOrDefault(a.getStatus(), a.getStatus()),
                a.getRejectReason() == null ? "" : a.getRejectReason(),
                a.getApplyTime() == null ? "" : a.getApplyTime().toString(),
                a.getReviewTime() == null ? "" : a.getReviewTime().toString()
            });
        }
        return rows;
    }
}
