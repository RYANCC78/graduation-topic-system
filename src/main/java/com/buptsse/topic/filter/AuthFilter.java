package com.buptsse.topic.filter;

import com.buptsse.topic.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("currentUser");

        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if (path.startsWith("/student/") && !"student".equals(user.getRole())) {
            response.sendError(403, "无学生权限");
            return;
        }
        if (path.startsWith("/teacher/") && !"teacher".equals(user.getRole())) {
            response.sendError(403, "无教师权限");
            return;
        }
        if (path.startsWith("/admin/") && !"admin".equals(user.getRole())) {
            response.sendError(403, "无管理员权限");
            return;
        }
        chain.doFilter(req, resp);
    }
}
