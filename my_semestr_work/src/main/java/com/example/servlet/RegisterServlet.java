package com.example.servlet;

import com.example.dto.SessionUser;
import com.example.dto.SignUpForm;
import com.example.exeptions.ValidationExeption;
import com.example.models.User;
import com.example.repository.CitiesRepository;
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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private SignUpService signUpService;
    private CitiesRepository citiesRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        citiesRepository = (CitiesRepository) config.getServletContext().getAttribute("cities");
        signUpService = (SignUpService) config.getServletContext().getAttribute("signUp");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("cities",citiesRepository.findAll());
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SignUpForm signUpForm = SignUpForm.builder()
                .username(req.getParameter("username"))
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .passwordRepeat(req.getParameter("confirmPassword"))
                .build();

        try{
            try {
                signUpForm.setCity_id(Long.parseLong(req.getParameter("cityId")));
            } catch (NumberFormatException e) {
                signUpForm.setCity_id(null);
            }
            User user = signUpService.signUp(signUpForm);

            HttpSession oldSession = req.getSession(false);
            if(oldSession!=null){
                oldSession.invalidate();
            }

            req.getSession(true).setAttribute("user", SessionUser.from(user));
            resp.sendRedirect(req.getContextPath()+"/home");
        } catch (ValidationExeption e) {
            req.setAttribute("errors", e.getErrors());
            req.setAttribute("error", String.join(" ", e.getErrors().values()));
            doGet(req, resp);
        } catch (Exception e) {
        e.printStackTrace();
        getServletContext().log("Ошибка регистрации", e);

        String message = e.getMessage() == null ? e.getClass().getName() : e.getMessage();

        req.setAttribute("error", "Ошибка регистрации: " + message);
        doGet(req, resp);
    }
//        catch (Exception e) {
//            getServletContext().log("Ошибка регистрации", e);
//            req.setAttribute("error", "Не удалось зарегистрироваться. Проверь email и настройки базы данных.");
//            doGet(req, resp);
//        }
    }
}
