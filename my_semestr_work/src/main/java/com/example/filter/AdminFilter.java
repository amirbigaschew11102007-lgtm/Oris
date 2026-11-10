package com.example.filter;

import com.example.models.Role;
import com.example.models.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session == null) ? null : (User) session.getAttribute("user");
        if(user == null){
            if (req.getMethod().equals("GET")) {
                String target= req.getRequestURI();
                if(req.getQueryString() != null){
                    target += "?" + req.getQueryString();
                }
                req.getSession().setAttribute("redirectAfterLogin", target);
            }
            resp.sendRedirect(req.getContextPath() + "/login");
        }else if(user.getRole() != Role.ADMIN){
            resp.sendError(404);
        }else{
            chain.doFilter(req, resp);
        }
    }
}