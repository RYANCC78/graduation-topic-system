package com.buptsse.topic.controller;

import com.buptsse.topic.model.User;
import com.buptsse.topic.service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private LoginService loginService = new LoginService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        try {
            User u = loginService.login(account, password);
            request.getSession().setAttribute("currentUser", u);
            String target;
            switch (u.getRole()) {
                case "student": target = "/student/topics"; break;
                case "teacher":  target = "/teacher/topics"; break;
                case "admin":    target = "/admin/statistics"; break;
                default:         target = "/login.jsp";
            }
            response.sendRedirect(request.getContextPath() + target);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
