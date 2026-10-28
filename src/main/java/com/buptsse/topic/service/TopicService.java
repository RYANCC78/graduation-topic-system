package com.buptsse.topic.service;

import com.buptsse.topic.dao.ApplicationDao;
import com.buptsse.topic.dao.TopicDao;
import com.buptsse.topic.dao.impl.ApplicationDaoImpl;
import com.buptsse.topic.dao.impl.TopicDaoImpl;
import com.buptsse.topic.model.Topic;

import java.util.List;

public class TopicService {
    private TopicDao topicDao = new TopicDaoImpl();
    private ApplicationDao applicationDao = new ApplicationDaoImpl();

    public List<Topic> listPublished(String keyword) {
        return topicDao.findPublished(keyword);
    }

    public List<Topic> listByTeacher(Integer teacherId) {
        return topicDao.findByTeacherId(teacherId);
    }

    public List<Topic> listAll() {
        return topicDao.findAll();
    }

    public Topic getById(Integer topicId) {
        return topicDao.findById(topicId);
    }

    public void publish(Integer teacherId, String title, String description, String ability, Integer quota) {
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("课题名称不能为空");
        if (quota == null || quota < 1) throw new IllegalArgumentException("课题名额必须为正整数");
        Topic t = new Topic();
        t.setTitle(title.trim());
        t.setDescription(description);
        t.setRequiredAbility(ability);
        t.setQuota(quota);
        t.setTeacherId(teacherId);
        topicDao.insert(t);
    }

    public void edit(Integer teacherId, Integer topicId, String title, String description, String ability) {
        Topic t = topicDao.findById(topicId);
        if (t == null || !t.getTeacherId().equals(teacherId)) {
            throw new IllegalArgumentException("只能编辑本人发布的课题");
        }
        if (!"published".equals(t.getStatus())) {
            throw new IllegalArgumentException("已撤销的课题不能编辑");
        }
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("课题名称不能为空");
        t.setTitle(title.trim());
        t.setDescription(description);
        t.setRequiredAbility(ability);
        t.setTeacherId(teacherId);
        topicDao.update(t);
    }

    public void revoke(Integer teacherId, Integer topicId) {
        Topic t = topicDao.findById(topicId);
        if (t == null || !t.getTeacherId().equals(teacherId)) {
            throw new IllegalArgumentException("只能撤销本人发布的课题");
        }
        if (!"published".equals(t.getStatus())) {
            throw new IllegalArgumentException("课题已撤销");
        }
        topicDao.updateStatus(topicId, "revoked");
        applicationDao.invalidatePendingByTopicId(topicId);
    }
}
