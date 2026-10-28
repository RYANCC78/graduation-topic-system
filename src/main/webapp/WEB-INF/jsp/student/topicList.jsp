<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.buptsse.topic.model.Topic, com.buptsse.topic.model.User" %>
<% User me = (User) session.getAttribute("currentUser"); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>课题浏览 - 毕业设计选题系统</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
<div class="layout">
    <div class="sidebar">
        <h2>学生端</h2>
        <a href="topics" class="active">课题浏览</a>
        <a href="my">我的选题</a>
    </div>
    <div class="main">
        <div class="topbar">
            <span class="user">当前用户：<%= me.getName() %>（<%= me.getAccount() %>）</span>
            <a class="btn btn-sm btn-gray" href="../logout">退出登录</a>
        </div>
        <div class="panel">
            <div class="toolbar">
                <form action="topics" method="get" style="display:flex;gap:10px;">
                    <input type="text" name="keyword" placeholder="按课题名称搜索" value="<%= request.getAttribute("keyword") == null ? "" : request.getAttribute("keyword") %>">
                    <button type="submit" class="btn btn-sm">搜索</button>
                </form>
                <span class="muted"><%= request.getAttribute("periodText") %></span>
                <% Boolean within = (Boolean) request.getAttribute("withinPeriod"); %>
                <% if (within != null && !within) { %>
                    <span class="badge badge-rejected">当前不在选题开放时间</span>
                <% } else { %>
                    <span class="badge badge-approved">选题开放中</span>
                <% } %>
            </div>
            <table>
                <tr><th>课题名称</th><th>简介</th><th>所需能力</th><th>指导教师</th><th>名额</th><th>剩余</th><th>操作</th></tr>
                <% List<Topic> topics = (List<Topic>) request.getAttribute("topics");
                   if (topics != null) for (Topic t : topics) { %>
                <tr>
                    <td><%= t.getTitle() %></td>
                    <td><%= t.getDescription() == null ? "" : t.getDescription() %></td>
                    <td><%= t.getRequiredAbility() == null ? "" : t.getRequiredAbility() %></td>
                    <td><%= t.getTeacherName() %></td>
                    <td><%= t.getQuota() %></td>
                    <td><%= t.getRemainingQuota() %></td>
                    <td>
                        <form action="apply" method="post" style="display:inline;"
                              onsubmit="return confirm('确认申请该课题？');">
                            <input type="hidden" name="topicId" value="<%= t.getTopicId() %>">
                            <button class="btn btn-sm" type="submit"
                                <% if (within == null || !within || t.getRemainingQuota() <= 0) { %>disabled<% } %>>
                                提交申请
                            </button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </table>
        </div>
    </div>
</div>
</body>
</html>
