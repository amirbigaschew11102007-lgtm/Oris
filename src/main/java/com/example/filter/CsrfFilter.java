package com.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

// Cross-Site Request Forgery.
// Проверяет защитный токен у каждого POST-запроса (в том числе AJAX и формы).

@WebFilter("/*")//только Post запросы проверяет
public class CsrfFilter implements Filter {
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        var session = request.getSession(true);
        String token = (String) session.getAttribute("csrfToken");
        if (token == null) {
            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute("csrfToken", token);
        }
        request.setAttribute("csrfToken", token);
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String submitted = request.getHeader("X-CSRF-Token");
            if (submitted == null) submitted = request.getParameter("_csrf");
            if (submitted == null || !MessageDigest.isEqual(token.getBytes(), submitted.getBytes())) {
                response.sendError(403, "Проверка CSRF-токена не пройдена.");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
