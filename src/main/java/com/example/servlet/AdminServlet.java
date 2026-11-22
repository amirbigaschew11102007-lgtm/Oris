package com.example.servlet;


import com.example.repository.UserRepository;
import com.example.service.AdminService;
import com.example.service.CategoriesService;
import com.example.service.PlacesService;
import com.example.service.UserService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private AdminService adminService;
    private PlacesService placesService;
    private UserService userService;
    private CategoriesService categoriesService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        adminService = (AdminService) config.getServletContext().getAttribute("adminService");
        placesService = (PlacesService) config.getServletContext().getAttribute("placesService");
        userService = (UserService) config.getServletContext().getAttribute("userRepository");
        categoriesService = (CategoriesService) config.getServletContext().getAttribute("categories");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            loadPage(req);
            req.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Не удалось открыть административную панель.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            switch (action == null ? "" : action) {
                case "deletePlace" -> {
                    long id = Long.parseLong(req.getParameter("id"));
                    adminService.deletePlace(id);
                }
                case "addCategory" -> {
                    adminService.addCategory(req.getParameter("name"));
                }
                default -> throw new IllegalArgumentException("Неизвестное действие администратора.");
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        } catch (Exception e) {
            throw new ServletException("Ошибка административного действия.", e);
        }
    }

    private void loadPage(HttpServletRequest request) throws Exception {
        request.setAttribute("places", placesService.allForAdmin());
        request.setAttribute("users", userService.findAll());
        request.setAttribute("categories", categoriesService.categories());
    }
}
