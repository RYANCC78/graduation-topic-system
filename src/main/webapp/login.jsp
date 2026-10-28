<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>毕业设计选题系统 - 登录</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="login-wrap">
    <h1>毕业设计选题系统</h1>
    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>
    <form action="login" method="post">
        <div class="form-item">
            <label>账号</label>
            <input type="text" name="account" required placeholder="学号/工号">
        </div>
        <div class="form-item">
            <label>密码</label>
            <input type="password" name="password" required placeholder="请输入密码">
        </div>
        <button type="submit" class="btn" style="width:100%;padding:12px;">登 录</button>
    </form>
    <p class="muted" style="margin-top:16px;text-align:center;">
        测试账号：admin/admin123 &nbsp; t001/teacher123 &nbsp; s2023001/student123
    </p>
</div>
</body>
</html>
