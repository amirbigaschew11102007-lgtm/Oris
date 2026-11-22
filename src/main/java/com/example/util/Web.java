package com.example.util;

import com.example.dto.ApiResponse;
import com.example.dto.SessionUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Locale;

public final class Web {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private Web(){

    }
    public static SessionUser user(HttpServletRequest request){
        return (SessionUser) request.getSession().getAttribute("user");
    }

    public static boolean isAjax(HttpServletRequest httpServletRequest){
        String reqWith = httpServletRequest.getHeader("X-Requested-With");
        String accept = httpServletRequest.getHeader("Accept");
        return "XMLHttpRequest".equalsIgnoreCase(reqWith) || (accept  != null && accept.toLowerCase().contains("application/json"));
    }

    public static void writeApiError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        OBJECT_MAPPER.writeValue(response.getWriter(), ApiResponse.builder()
                .ok(false)
                .error(message)
                .build());
    }
}
