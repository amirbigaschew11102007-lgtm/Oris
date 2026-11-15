package com.example.servlet;


import com.example.dto.SessionUser;
import com.example.dto.SignUpForm;
import com.example.exeptions.BusinessException;
import com.example.models.User;
import com.example.service.SignUpService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SignUpForm signUpForm = SignUpForm.builder()
                .username(req.getParameter("login"))
                .password(req.getParameter("password"))
//                .email(req.getParameter("email"))
                .build();
        try {
            User user = signUpService.signUp(signUpForm);
            HttpSession oldSession = req.getSession(false);

            if (oldSession != null) oldSession.invalidate();

            req.getSession(true).setAttribute("user", SessionUser.from(user));
            resp.sendRedirect(req.getContextPath() + "/home");
        } catch (BusinessException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        } catch (Exception e) {
            throw new ServletException("Ошибка входа.", e);
        }
    }
}
