<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.buptsse.topic.model.Application, com.buptsse.topic.model.User" %>
<% User me = (User) session.getAttribute("currentUser");
   Map<String,String> statusMap = new HashMap<>();
   statusMap.put("pending","待审核"); statusMap.put("approved","已通过");
   statusMap.put("rejected","已驳回"); statusMap.put("cancelled","已撤销"); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>我的选题 - 毕业设计选题系统</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
<div class="layout">
    <div class="sidebar">
        <h2>学生端</h2>
        <a href="topics">课题浏览</a>
        <a href="my" class="active">我的选题</a>
    </div>
    <div class="main">
        <div class="topbar">
            <span class="user">当前用户：<%= me.getName() %>（<%= me.getAccount() %>）</span>
            <a class="btn btn-sm btn-gray" href="../logout">退出登录</a>
        </div>
        <div class="panel">
            <h3>我的选题申请记录</h3>
            <% if (request.getAttribute("msg") != null) { %><div class="success"><%= request.getAttribute("msg") %></div><% } %>
            <% if (request.getAttribute("error") != null) { %><div class="error"><%= request.getAttribute("error") %></div><% } %>
            <table>
                <tr><th>课题名称</th><th>指导教师</th><th>申请时间</th><th>状态</th><th>驳回理由</th><th>操作</th></tr>
                <% List<Application> apps = (List<Application>) request.getAttribute("apps");
                   if (apps != null) for (Application a : apps) { %>
                <tr>
                    <td><%= a.getTopicTitle() %></td>
                    <td><%= a.getTeacherName() %></td>
                    <td><%= a.getApplyTime() %></td>
                    <td><span class="badge badge-<%= a.getStatus() %>"><%= statusMap.get(a.getStatus()) %></span></td>
                    <td><%= a.getRejectReason() == null ? "" : a.getRejectReason() %></td>
                    <td>
                        <% if ("pending".equals(a.getStatus())) { %>
                        <form action="cancel" method="post" style="display:inline;" onsubmit="return confirm('确认撤销该申请？');">
                            <input type="hidden" name="applicationId" value="<%= a.getApplicationId() %>">
                            <button class="btn btn-sm btn-danger" type="submit">撤销申请</button>
                        </form>
                        <% } else { %>
                        <span class="muted">—</span>
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
