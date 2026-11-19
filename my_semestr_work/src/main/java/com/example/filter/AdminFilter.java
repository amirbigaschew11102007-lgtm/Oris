package com.example.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.example.dto.SessionUser;
import com.example.models.Role;
import com.example.models.User;
import com.example.repository.UserRepository;

import java.io.IOException;

// Не просто скрывает ссылку на админку, а проверяет роль на сервере при каждом запросе.
@WebFilter("/admin/*")
public class AdminFilter implements Filter {
    private UserRepository usersRepository;

    @Override
    public void init(FilterConfig filterConfig) {
        usersRepository = (UserRepository) filterConfig.getServletContext().getAttribute("userRepository");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        SessionUser sessionUser = session == null ? null : (SessionUser) session.getAttribute("user");

        if (sessionUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            User current = usersRepository.findById(sessionUser.getId()).orElse(null);
            if (current == null) {
                session.invalidate();
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            if (current.getRole()== Role.ADMIN) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ разрешён только администратору.");
                return;
            }

            session.setAttribute("user", SessionUser.from(current));
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw new ServletException("Не удалось проверить права администратора.", e);
        }
    }
}
