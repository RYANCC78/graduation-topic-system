package com.buptsse.topic.model;

import java.sql.Timestamp;

public class Application {
    private Integer applicationId;
    private Integer studentId;
    private String studentName;
    private Integer topicId;
    private String topicTitle;
    private String teacherName;
    private String status;
    private String rejectReason;
    private Timestamp applyTime;
    private Timestamp reviewTime;

    public Integer getApplicationId() { return applicationId; }
    public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }
    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public Integer getTopicId() { return topicId; }
    public void setTopicId(Integer topicId) { this.topicId = topicId; }
    public String getTopicTitle() { return topicTitle; }
    public void setTopicTitle(String topicTitle) { this.topicTitle = topicTitle; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
    public Timestamp getApplyTime() { return applyTime; }
    public void setApplyTime(Timestamp applyTime) { this.applyTime = applyTime; }
    public Timestamp getReviewTime() { return reviewTime; }
    public void setReviewTime(Timestamp reviewTime) { this.reviewTime = reviewTime; }
}
