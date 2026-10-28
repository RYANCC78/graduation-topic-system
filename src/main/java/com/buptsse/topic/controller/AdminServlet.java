package com.buptsse.topic.controller;

import com.buptsse.topic.model.User;
import com.buptsse.topic.service.AccountService;
import com.buptsse.topic.service.ConfigService;
import com.buptsse.topic.service.StatisticsService;
import com.buptsse.topic.util.ExcelUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private AccountService accountService = new AccountService();
    private StatisticsService statisticsService = new StatisticsService();
    private ConfigService configService = new ConfigService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo() == null ? "/statistics" : request.getPathInfo();
        switch (path) {
            case "/statistics": {
                Map<String, Object> overview = statisticsService.overview();
                request.setAttribute("overview", overview);
                request.setAttribute("apps", statisticsService.allApplications());
                request.getRequestDispatcher("/WEB-INF/jsp/admin/statistics.jsp").forward(request, response);
                break;
            }
            case "/accounts": {
                String role = request.getParameter("role");
                String keyword = request.getParameter("keyword");
                List<User> users = accountService.list(role, keyword);
                request.setAttribute("users", users);
                request.setAttribute("role", role);
                request.setAttribute("keyword", keyword);
                request.getRequestDispatcher("/WEB-INF/jsp/admin/accountManage.jsp").forward(request, response);
                break;
            }
            case "/config": {
                request.setAttribute("start", configService.getStart());
                request.setAttribute("end", configService.getEnd());
                request.getRequestDispatcher("/WEB-INF/jsp/admin/config.jsp").forward(request, response);
                break;
            }
            case "/export": {
                try {
                    ExcelUtil.export(response, "选题统计.xlsx",
                            statisticsService.exportHeaders(), statisticsService.exportRows());
                } catch (Exception e) {
                    throw new ServletException(e);
                }
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
        try {
            if ("/accountAdd".equals(path)) {
                accountService.add(request.getParameter("account"),
                        request.getParameter("password"),
                        request.getParameter("role"),
                        request.getParameter("name"),
                        request.getParameter("phone"),
                        request.getParameter("email"));
                request.setAttribute("msg", "账号新增成功");
            } else if ("/accountEdit".equals(path)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                accountService.edit(userId,
                        request.getParameter("name"),
                        request.getParameter("phone"),
                        request.getParameter("email"));
                request.setAttribute("msg", "账号信息已更新");
            } else if ("/accountToggle".equals(path)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                accountService.toggleStatus(userId);
                request.setAttribute("msg", "账号状态已切换");
            } else if ("/config".equals(path)) {
                configService.updatePeriod(request.getParameter("startTime"),
                        request.getParameter("endTime"));
                request.setAttribute("msg", "选题时间已更新");
            } else {
                response.sendError(404);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "参数错误");
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        }
        if ("/config".equals(path)) {
            request.setAttribute("start", configService.getStart());
            request.setAttribute("end", configService.getEnd());
            request.getRequestDispatcher("/WEB-INF/jsp/admin/config.jsp").forward(request, response);
        } else {
            List<User> users = accountService.list(null, null);
            request.setAttribute("users", users);
            request.getRequestDispatcher("/WEB-INF/jsp/admin/accountManage.jsp").forward(request, response);
        }
    }
}
