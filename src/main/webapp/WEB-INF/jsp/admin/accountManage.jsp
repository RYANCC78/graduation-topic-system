<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.buptsse.topic.model.User" %>
<% User me = (User) session.getAttribute("currentUser");
   Map<String,String> roleMap = new HashMap<>();
   roleMap.put("student","学生"); roleMap.put("teacher","教师"); %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>账号管理 - 毕业设计选题系统</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
<div class="layout">
    <div class="sidebar">
        <h2>管理员端</h2>
        <a href="statistics">选题统计</a>
        <a href="accounts" class="active">账号管理</a>
        <a href="config">系统配置</a>
    </div>
    <div class="main">
        <div class="topbar">
            <span class="user">当前用户：<%= me.getName() %>（<%= me.getAccount() %>）</span>
            <a class="btn btn-sm btn-gray" href="../logout">退出登录</a>
        </div>

        <div class="panel">
            <h3>新增账号</h3>
            <% if (request.getAttribute("msg") != null) { %><div class="success"><%= request.getAttribute("msg") %></div><% } %>
            <% if (request.getAttribute("error") != null) { %><div class="error"><%= request.getAttribute("error") %></div><% } %>
            <form action="accountAdd" method="post">
                <div class="form-row"><label>登录账号</label><input type="text" name="account" required></div>
                <div class="form-row"><label>初始密码</label><input type="text" name="password" required placeholder="至少6位"></div>
                <div class="form-row"><label>角色</label>
                    <select name="role">
                        <option value="student">学生</option>
                        <option value="teacher">教师</option>
                    </select>
                </div>
                <div class="form-row"><label>姓名</label><input type="text" name="name" required></div>
                <div class="form-row"><label>电话</label><input type="text" name="phone"></div>
                <div class="form-row"><label>邮箱</label><input type="text" name="email"></div>
                <button type="submit" class="btn">新增账号</button>
            </form>
        </div>

        <div class="panel">
            <h3>账号列表</h3>
            <div class="toolbar">
                <form action="accounts" method="get" style="display:flex;gap:10px;">
                    <select name="role">
                        <option value="">全部角色</option>
                        <option value="student">学生</option>
                        <option value="teacher">教师</option>
                    </select>
                    <input type="text" name="keyword" placeholder="账号/姓名">
                    <button class="btn btn-sm">筛选</button>
                </form>
            </div>
            <table>
                <tr><th>账号</th><th>姓名</th><th>角色</th><th>电话</th><th>邮箱</th><th>状态</th><th>操作</th></tr>
                <% List<User> users = (List<User>) request.getAttribute("users");
                   if (users != null) for (User u : users) { %>
                <tr>
                    <td><%= u.getAccount() %></td>
                    <td><%= u.getName() %></td>
                    <td><%= roleMap.get(u.getRole()) %></td>
                    <td><%= u.getPhone() == null ? "" : u.getPhone() %></td>
                    <td><%= u.getEmail() == null ? "" : u.getEmail() %></td>
                    <td>
                        <% if (u.getStatus() != null && u.getStatus() == 1) { %>
                            <span class="badge badge-on">启用</span>
                        <% } else { %>
                            <span class="badge badge-off">禁用</span>
                        <% } %>
                    </td>
                    <td>
                        <form action="accountToggle" method="post" style="display:inline;">
                            <input type="hidden" name="userId" value="<%= u.getUserId() %>">
                            <button class="btn btn-sm btn-gray" type="submit">
                                <%= (u.getStatus() != null && u.getStatus() == 1) ? "禁用" : "启用" %>
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
