package com.example.servlets;

import com.example.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

//http://localhost:8080/homeWork_3_war/search?field=name&value=Amir
@WebServlet("/search")
public class UsersSearchServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String field = request.getParameter("field");
        String value = request.getParameter("value");

        response.setContentType("text/plain;charset=UTF-8");

        try {
            switch (field) {
                case "name" -> printUsers(
                        response,
                        userDao.findByName(value),
                        "Поиск по имени"
                );

                case "email" -> printUsers(
                        response,
                        userDao.findByEmail(value),
                        "Поиск по email"
                );

                case "city" -> printUsers(
                        response,
                        userDao.findByCity(value),
                        "Поиск по городу"
                );

                default -> response.getWriter().println(
                        "Неверное поле. Доступны: name, email, city"
                );
            }

        } catch (Exception e) {
            throw new ServletException(e);
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