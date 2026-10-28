package com.buptsse.topic.controller;

import com.buptsse.topic.model.Application;
import com.buptsse.topic.model.Topic;
import com.buptsse.topic.model.User;
import com.buptsse.topic.service.ApplicationService;
import com.buptsse.topic.service.TopicService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/teacher/*")
public class TeacherServlet extends HttpServlet {
    private TopicService topicService = new TopicService();
    private ApplicationService applicationService = new ApplicationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo() == null ? "/topics" : request.getPathInfo();
        User me = (User) request.getSession().getAttribute("currentUser");
        switch (path) {
            case "/topics": {
                List<Topic> topics = topicService.listByTeacher(me.getUserId());
                request.setAttribute("topics", topics);
                request.getRequestDispatcher("/WEB-INF/jsp/teacher/topicManage.jsp").forward(request, response);
                break;
            }
            case "/review": {
                List<Application> apps = applicationService.listByTeacher(me.getUserId());
                request.setAttribute("apps", apps);
                request.getRequestDispatcher("/WEB-INF/jsp/teacher/reviewList.jsp").forward(request, response);
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
            if ("/publish".equals(path)) {
                String title = request.getParameter("title");
                String desc = request.getParameter("description");
                String ability = request.getParameter("requiredAbility");
                int quota = Integer.parseInt(request.getParameter("quota"));
                topicService.publish(me.getUserId(), title, desc, ability, quota);
                request.setAttribute("msg", "课题发布成功");
            } else if ("/edit".equals(path)) {
                int topicId = Integer.parseInt(request.getParameter("topicId"));
                String title = request.getParameter("title");
                String desc = request.getParameter("description");
                String ability = request.getParameter("requiredAbility");
                topicService.edit(me.getUserId(), topicId, title, desc, ability);
                request.setAttribute("msg", "课题已更新");
            } else if ("/revoke".equals(path)) {
                int topicId = Integer.parseInt(request.getParameter("topicId"));
                topicService.revoke(me.getUserId(), topicId);
                request.setAttribute("msg", "课题已撤销，待审核申请已全部作废");
            } else if ("/reviewOp".equals(path)) {
                int appId = Integer.parseInt(request.getParameter("applicationId"));
                boolean approve = "approve".equals(request.getParameter("action"));
                String reason = request.getParameter("rejectReason");
                applicationService.review(me.getUserId(), appId, approve, reason);
                request.setAttribute("msg", approve ? "已同意该生选题" : "已驳回该申请");
            } else {
                response.sendError(404);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "参数错误：请检查名额等数字字段");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        }
        String forward = "/teacher/review".equals(path) || "/reviewOp".equals(path)
                ? "/WEB-INF/jsp/teacher/reviewList.jsp" : "/WEB-INF/jsp/teacher/topicManage.jsp";
        if ("/reviewOp".equals(path)) {
            request.setAttribute("apps", applicationService.listByTeacher(me.getUserId()));
        } else if (!"/review".equals(path)) {
            request.setAttribute("topics", topicService.listByTeacher(me.getUserId()));
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }
}
