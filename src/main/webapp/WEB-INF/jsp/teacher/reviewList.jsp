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
    <title>申请审核 - 毕业设计选题系统</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
<div class="layout">
    <div class="sidebar">
        <h2>教师端</h2>
        <a href="topics">课题管理</a>
        <a href="review" class="active">申请审核</a>
    </div>
    <div class="main">
        <div class="topbar">
            <span class="user">当前用户：<%= me.getName() %>（<%= me.getAccount() %>）</span>
            <a class="btn btn-sm btn-gray" href="../logout">退出登录</a>
        </div>
        <div class="panel">
            <h3>选题申请审核</h3>
            <% if (request.getAttribute("msg") != null) { %><div class="success"><%= request.getAttribute("msg") %></div><% } %>
            <% if (request.getAttribute("error") != null) { %><div class="error"><%= request.getAttribute("error") %></div><% } %>
            <table>
                <tr><th>学生姓名</th><th>申请课题</th><th>申请时间</th><th>状态</th><th>驳回理由</th><th>操作</th></tr>
                <% List<Application> apps = (List<Application>) request.getAttribute("apps");
                   if (apps != null) for (Application a : apps) { %>
                <tr>
                    <td><%= a.getStudentName() %></td>
                    <td><%= a.getTopicTitle() %></td>
                    <td><%= a.getApplyTime() %></td>
                    <td><span class="badge badge-<%= a.getStatus() %>"><%= statusMap.get(a.getStatus()) %></span></td>
                    <td><%= a.getRejectReason() == null ? "" : a.getRejectReason() %></td>
                    <td>
                        <% if ("pending".equals(a.getStatus())) { %>
                        <form action="reviewOp" method="post" style="display:inline;">
                            <input type="hidden" name="applicationId" value="<%= a.getApplicationId() %>">
                            <button class="btn btn-sm" name="action" value="approve"
                                onclick="return confirm('确认同意该生选题？');">同意</button>
                            <input type="text" name="rejectReason" placeholder="驳回理由（驳回时必填）" style="width:180px;padding:4px;">
                            <button class="btn btn-sm btn-danger" name="action" value="reject"
                                onclick="return confirm('确认驳回该申请？');">驳回</button>
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
