package com.example.servlets;

import com.example.dao.UserDao;
import com.example.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/save")
public class UsersSaveServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int count = userDao.getUsersToAdd();
            request.setAttribute("count", count);

            request.getRequestDispatcher("/save.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] names = request.getParameterValues("name");
        String[] emails = request.getParameterValues("email");
        String[] ages = request.getParameterValues("age");
        String[] cities = request.getParameterValues("city");

        if (names == null || emails == null || ages == null || cities == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Не переданы все поля формы: name, email, age, city");
            return;
        }

        try {
            List<User> users = new ArrayList<>();

            for (int i = 0; i < names.length; i++) {
                users.add(new User(
                        names[i],
                        emails[i],
                        Integer.parseInt(ages[i]),
                        cities[i]
                ));
            }

            for (User user : users) {
                userDao.saveUsers(user);
            }
            //сразу перенаправляет по текущей ссылке+html фал
            response.sendRedirect(request.getContextPath() + "/success.html");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}