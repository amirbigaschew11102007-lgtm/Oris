package com.example.servlet;

import com.example.models.Categories;
import com.example.repository.CategoriesRepository;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/categories")
public class CategoriesServlet extends HttpServlet {
    private CategoriesRepository categoriesRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        categoriesRepository = (CategoriesRepository) config.getServletContext().getAttribute("categories");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String value = req.getParameter("value");
        try {
            List<Categories> categories = categoriesRepository.findAll(value);
            if (!categories.isEmpty()) {
                printUsers(resp, categories, "Halloween");
            } else {
                resp.getWriter().println(
                        "Categories not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printUsers(HttpServletResponse response, List<?> users, String title
    ) throws IOException {

        System.out.println("=== " + title + " ===");
        System.out.println(users);

        response.getWriter().println(title);
        response.getWriter().println(users);
    }
}
