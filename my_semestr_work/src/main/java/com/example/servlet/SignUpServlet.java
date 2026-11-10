package com.example.servlet;


import com.example.dto.SignUpForm;
import com.example.service.SignUpService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet{
    private SignUpService signUpService;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        signUpService = (SignUpService) config.getServletContext().getAttribute("signUp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/html/signUp.html").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SignUpForm signUpForm = SignUpForm.builder()
                .login(req.getParameter("login"))
                .password(req.getParameter("password"))
                .email(req.getParameter("email"))
                .build();
        try {
            signUpService.signUp(signUpForm);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
