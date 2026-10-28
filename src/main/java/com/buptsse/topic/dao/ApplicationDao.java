package com.buptsse.topic.dao;

import com.buptsse.topic.model.Application;
import java.util.List;

public interface ApplicationDao {
    Application findPendingByStudentId(Integer studentId);
    Application findApprovedByStudentId(Integer studentId);
    List<Application> findByStudentId(Integer studentId);
    List<Application> findByTeacherId(Integer teacherId);
    Application findById(Integer applicationId);
    List<Application> findAllJoined();
    void insert(Application application);
    void updateStatus(Integer applicationId, String status, String rejectReason);
    void invalidatePendingByTopicId(Integer topicId);
}
