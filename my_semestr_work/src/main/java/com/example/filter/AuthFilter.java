package com.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(
        filterName = "authFilter",
        urlPatterns = "/*"
)
public class AuthFilter implements Filter {

    public AuthFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(
                req.getContextPath().length()
        );

        // Публичные страницы: логин, регистрация, статика
        boolean publicPath = path.equals("/login")
                || path.equals("/signUp")
                || path.equals("/categories")
                || path.startsWith("/WEB-INF/");

        if (publicPath) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            // Пользователь не вошёл
            resp.sendRedirect(req.getContextPath() + "/signUp");
            return;
        }

        String role = (String) session.getAttribute("role");

        // Админка доступна только ADMIN
        if (path.startsWith("/admin") && !"ADMIN".equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Доступ запрещён: требуется роль ADMIN");
            return;
        }

        chain.doFilter(request, response);
    }
}