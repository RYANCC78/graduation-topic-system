package com.buptsse.topic.controller;

import com.buptsse.topic.model.Application;
import com.buptsse.topic.model.Topic;
import com.buptsse.topic.model.User;
import com.buptsse.topic.service.ApplicationService;
import com.buptsse.topic.service.ConfigService;
import com.buptsse.topic.service.TopicService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/student/*")
public class StudentServlet extends HttpServlet {
    private TopicService topicService = new TopicService();
    private ApplicationService applicationService = new ApplicationService();
    private ConfigService configService = new ConfigService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo() == null ? "/topics" : request.getPathInfo();
        User me = (User) request.getSession().getAttribute("currentUser");
        switch (path) {
            case "/topics": {
                String keyword = request.getParameter("keyword");
                List<Topic> topics = topicService.listPublished(keyword);
                request.setAttribute("topics", topics);
                request.setAttribute("keyword", keyword);
                request.setAttribute("withinPeriod", configService.isWithinSelectionPeriod());
                request.setAttribute("periodText", configService.getPeriodText());
                request.getRequestDispatcher("/WEB-INF/jsp/student/topicList.jsp").forward(request, response);
                break;
            }
            case "/my": {
                List<Application> apps = applicationService.listByStudent(me.getUserId());
                request.setAttribute("apps", apps);
                request.getRequestDispatcher("/WEB-INF/jsp/student/myApplication.jsp").forward(request, response);
                break;
            }
            default:
                response.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getPathInfo() == null ? "" : request.getPathInfo();
        User me = (User) request.getSession().getAttribute("currentUser");
        try {
            if ("/apply".equals(path)) {
                Integer topicId = Integer.parseInt(request.getParameter("topicId"));
                applicationService.apply(me.getUserId(), topicId);
                request.setAttribute("msg", "选题申请已提交，请等待教师审核");
            } else if ("/cancel".equals(path)) {
                Integer appId = Integer.parseInt(request.getParameter("applicationId"));
                applicationService.cancel(me.getUserId(), appId);
                request.setAttribute("msg", "申请已撤销");
            } else {
                response.sendError(404);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "参数错误");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        }
        List<Application> apps = applicationService.listByStudent(me.getUserId());
        request.setAttribute("apps", apps);
        request.getRequestDispatcher("/WEB-INF/jsp/student/myApplication.jsp").forward(request, response);
    }
}
