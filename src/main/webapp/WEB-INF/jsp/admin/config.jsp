<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.buptsse.topic.model.User" %>
<% User me = (User) session.getAttribute("currentUser"); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>系统配置 - 毕业设计选题系统</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
<div class="layout">
    <div class="sidebar">
        <h2>管理员端</h2>
        <a href="statistics">选题统计</a>
        <a href="accounts">账号管理</a>
        <a href="config" class="active">系统配置</a>
    </div>
    <div class="main">
        <div class="topbar">
            <span class="user">当前用户：<%= me.getName() %>（<%= me.getAccount() %>）</span>
            <a class="btn btn-sm btn-gray" href="../logout">退出登录</a>
        </div>
        <div class="panel">
            <h3>选题时间配置</h3>
            <% if (request.getAttribute("msg") != null) { %><div class="success"><%= request.getAttribute("msg") %></div><% } %>
            <% if (request.getAttribute("error") != null) { %><div class="error"><%= request.getAttribute("error") %></div><% } %>
            <p class="muted" style="margin-bottom:16px;">设置选题开放起止时间，不在该时间段内学生无法提交选题申请。</p>
            <form action="config" method="post">
                <div class="form-row"><label>开始时间</label><input type="text" name="startTime" value="<%= request.getAttribute("start") %>" placeholder="yyyy-MM-dd HH:mm:ss"></div>
                <div class="form-row"><label>结束时间</label><input type="text" name="endTime" value="<%= request.getAttribute("end") %>" placeholder="yyyy-MM-dd HH:mm:ss"></div>
                <button type="submit" class="btn">保存配置</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
