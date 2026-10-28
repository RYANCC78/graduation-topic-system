package com.buptsse.topic.dao;

import com.buptsse.topic.model.Topic;
import java.util.List;

public interface TopicDao {
    List<Topic> findPublished(String keyword);
    List<Topic> findByTeacherId(Integer teacherId);
    List<Topic> findAll();
    Topic findById(Integer topicId);
    void insert(Topic topic);
    void update(Topic topic);
    void updateStatus(Integer topicId, String status);
    void decreaseQuota(Integer topicId);
    void increaseQuota(Integer topicId);
}
