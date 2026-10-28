<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.buptsse.topic.model.Application, com.buptsse.topic.model.User" %>
<% User me = (User) session.getAttribute("currentUser");
   Map<String,String> statusMap = new HashMap<>();
   statusMap.put("pending","待审核"); statusMap.put("approved","已通过");
   statusMap.put("rejected","已驳回"); statusMap.put("cancelled","已撤销");
   Map<String,Object> ov = (Map<String,Object>) request.getAttribute("overview"); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>选题统计 - 毕业设计选题系统</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
<div class="layout">
    <div class="sidebar">
        <h2>管理员端</h2>
        <a href="statistics" class="active">选题统计</a>
        <a href="accounts">账号管理</a>
        <a href="config">系统配置</a>
    </div>
    <div class="main">
        <div class="topbar">
            <span class="user">当前用户：<%= me.getName() %>（<%= me.getAccount() %>）</span>
            <a class="btn btn-sm btn-gray" href="../logout">退出登录</a>
        </div>
        <div class="stats-row">
            <div class="stat-card"><div class="num"><%= ov.get("topicCount") %></div><div class="label">课题总数</div></div>
            <div class="stat-card"><div class="num"><%= ov.get("studentCount") %></div><div class="label">学生数</div></div>
            <div class="stat-card"><div class="num"><%= ov.get("teacherCount") %></div><div class="label">教师数</div></div>
            <div class="stat-card"><div class="num"><%= ov.get("totalApps") %></div><div class="label">申请总数</div></div>
            <div class="stat-card"><div class="num"><%= ov.get("approved") %></div><div class="label">已通过</div></div>
            <div class="stat-card"><div class="num"><%= ov.get("pending") %></div><div class="label">待审核</div></div>
        </div>
        <div class="panel">
            <h3>全部申请记录 <a class="btn btn-sm" href="export" style="float:right;">导出 Excel</a></h3>
            <table>
                <tr><th>编号</th><th>学生</th><th>课题</th><th>指导教师</th><th>状态</th><th>申请时间</th><th>审核时间</th></tr>
                <% List<Application> apps = (List<Application>) request.getAttribute("apps");
                   if (apps != null) for (Application a : apps) { %>
                <tr>
                    <td><%= a.getApplicationId() %></td>
                    <td><%= a.getStudentName() %></td>
                    <td><%= a.getTopicTitle() %></td>
                    <td><%= a.getTeacherName() %></td>
                    <td><span class="badge badge-<%= a.getStatus() %>"><%= statusMap.get(a.getStatus()) %></span></td>
                    <td><%= a.getApplyTime() %></td>
                    <td><%= a.getReviewTime() == null ? "" : a.getReviewTime() %></td>
                </tr>
                <% } %>
            </table>
        </div>
    </div>
</div>
</body>
</html>
