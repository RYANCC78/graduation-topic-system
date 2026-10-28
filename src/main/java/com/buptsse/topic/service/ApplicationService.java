package com.buptsse.topic.service;

import com.buptsse.topic.dao.ApplicationDao;
import com.buptsse.topic.dao.TopicDao;
import com.buptsse.topic.dao.impl.ApplicationDaoImpl;
import com.buptsse.topic.dao.impl.TopicDaoImpl;
import com.buptsse.topic.model.Application;
import com.buptsse.topic.model.Topic;

import java.util.List;

public class ApplicationService {
    private ApplicationDao applicationDao = new ApplicationDaoImpl();
    private TopicDao topicDao = new TopicDaoImpl();
    private ConfigService configService = new ConfigService();

    public void apply(Integer studentId, Integer topicId) {
        if (topicId == null) throw new IllegalArgumentException("请选择要申请的课题");
        if (!configService.isWithinSelectionPeriod()) {
            throw new IllegalArgumentException("当前不在选题开放时间内，无法提交申请");
        }
        if (applicationDao.findApprovedByStudentId(studentId) != null) {
            throw new IllegalArgumentException("你已确定选题，不能重复申请");
        }
        if (applicationDao.findPendingByStudentId(studentId) != null) {
            throw new IllegalArgumentException("你已有待审核的申请，请勿重复提交");
        }
        Topic t = topicDao.findById(topicId);
        if (t == null || !"published".equals(t.getStatus())) {
            throw new IllegalArgumentException("课题不存在或已被撤销");
        }
        if (t.getRemainingQuota() == null || t.getRemainingQuota() <= 0) {
            throw new IllegalArgumentException("该课题名额已满");
        }
        Application a = new Application();
        a.setStudentId(studentId);
        a.setTopicId(topicId);
        applicationDao.insert(a);
    }

    public void cancel(Integer studentId, Integer applicationId) {
        Application a = applicationDao.findById(applicationId);
        if (a == null || !a.getStudentId().equals(studentId)) {
            throw new IllegalArgumentException("申请不存在");
        }
        if (!"pending".equals(a.getStatus())) {
            throw new IllegalArgumentException("只有待审核的申请可以撤销");
        }
        applicationDao.updateStatus(applicationId, "cancelled", "学生主动撤销");
    }

    public void review(Integer teacherId, Integer applicationId, boolean approve, String reason) {
        Application a = applicationDao.findById(applicationId);
        if (a == null) throw new IllegalArgumentException("申请不存在");
        Topic t = topicDao.findById(a.getTopicId());
        if (t == null || !t.getTeacherId().equals(teacherId)) {
            throw new IllegalArgumentException("只能审核本人课题下的申请");
        }
        if (!"pending".equals(a.getStatus())) {
            throw new IllegalArgumentException("该申请已处理");
        }
        if (approve) {
            if (t.getRemainingQuota() == null || t.getRemainingQuota() <= 0) {
                throw new IllegalArgumentException("该课题名额已满，无法同意");
            }
            applicationDao.updateStatus(applicationId, "approved", null);
            topicDao.decreaseQuota(a.getTopicId());
        } else {
            if (reason == null || reason.trim().isEmpty()) {
                throw new IllegalArgumentException("驳回时必须填写理由");
            }
            applicationDao.updateStatus(applicationId, "rejected", reason.trim());
        }
    }

    public List<Application> listByStudent(Integer studentId) {
        return applicationDao.findByStudentId(studentId);
    }

    public List<Application> listByTeacher(Integer teacherId) {
        return applicationDao.findByTeacherId(teacherId);
    }
}
