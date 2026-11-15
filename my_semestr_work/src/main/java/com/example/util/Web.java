package com.example.util;

import com.example.dto.SessionUser;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

public final class Web {
    private Web(){

    }
    public static SessionUser user(HttpServletRequest request){
        return (SessionUser) request.getSession().getAttribute("user");
    }
}
