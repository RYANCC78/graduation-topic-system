<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.buptsse.topic.model.Topic, com.buptsse.topic.model.User" %>
<% User me = (User) session.getAttribute("currentUser"); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>课题管理 - 毕业设计选题系统</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
<div class="layout">
    <div class="sidebar">
        <h2>教师端</h2>
        <a href="topics" class="active">课题管理</a>
        <a href="review">申请审核</a>
    </div>
    <div class="main">
        <div class="topbar">
            <span class="user">当前用户：<%= me.getName() %>（<%= me.getAccount() %>）</span>
            <a class="btn btn-sm btn-gray" href="../logout">退出登录</a>
        </div>

        <div class="panel">
            <h3>发布新课题</h3>
            <% if (request.getAttribute("msg") != null) { %><div class="success"><%= request.getAttribute("msg") %></div><% } %>
            <% if (request.getAttribute("error") != null) { %><div class="error"><%= request.getAttribute("error") %></div><% } %>
            <form action="publish" method="post">
                <div class="form-row"><label>课题名称</label><input type="text" name="title" required></div>
                <div class="form-row"><label>课题简介</label><textarea name="description"></textarea></div>
                <div class="form-row"><label>所需能力</label><input type="text" name="requiredAbility"></div>
                <div class="form-row"><label>课题名额</label><input type="number" name="quota" min="1" value="1" required></div>
                <button type="submit" class="btn">发布课题</button>
            </form>
        </div>

        <div class="panel">
            <h3>我发布的课题</h3>
            <table>
                <tr><th>课题名称</th><th>简介</th><th>名额</th><th>剩余</th><th>状态</th><th>发布时间</th><th>操作</th></tr>
                <% List<Topic> topics = (List<Topic>) request.getAttribute("topics");
                   if (topics != null) for (Topic t : topics) { %>
                <tr>
                    <td><%= t.getTitle() %></td>
                    <td><%= t.getDescription() == null ? "" : t.getDescription() %></td>
                    <td><%= t.getQuota() %></td>
                    <td><%= t.getRemainingQuota() %></td>
                    <td><span class="badge badge-<%= t.getStatus() %>"><%= "published".equals(t.getStatus()) ? "已发布" : "已撤销" %></span></td>
                    <td><%= t.getCreateTime() %></td>
                    <td>
                        <% if ("published".equals(t.getStatus())) { %>
                        <form action="revoke" method="post" style="display:inline;" onsubmit="return confirm('撤销后该课题下待审核申请将全部作废，确认？');">
                            <input type="hidden" name="topicId" value="<%= t.getTopicId() %>">
                            <button class="btn btn-sm btn-danger" type="submit">撤销</button>
                        </form>
                        <% } %>
                    </td>
                </tr>
                <% } %>
            </table>
        </div>
    </div>
</div>
</body>
</html>
