package com.example.servlet;

import com.example.dto.ApiResponse;
import com.example.dto.SessionUser;
import com.example.models.Comment;
import com.example.service.CommentsService;
import com.example.util.MultipartFileReader;
import com.example.util.Web;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/comments")
@MultipartConfig(maxFileSize = 5*1024*1024,maxRequestSize = 6*1024*1024)
public class CommentServlet extends HttpServlet {
    private CommentsService commentsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        commentsService = (CommentsService) config.getServletContext().getAttribute("commentsService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        SessionUser user = Web.user(req);

        try{
            long photoId = Long.parseLong(req.getParameter("photoId"));
            Comment comment = commentsService.add(
                    photoId, user.getId(),req.getParameter("content"), MultipartFileReader.read(req.getPart("attachment"))
            );

            Map<String,Object> data = new LinkedHashMap<>();
            data.put("id",comment.getId());
            data.put("username",user.getUsername());
            data.put("content",comment.getContent());
            data.put("attachmentUrl",comment.getAttachment_path() == null
            ? null : req.getContextPath()+"/media/"+comment.getAttachment_path());

            ApiResponse body = ApiResponse.builder()
                    .ok(true)
                    .data(data)
                    .build();
            objectMapper.writeValue(resp.getWriter(),body);
        }catch (IllegalArgumentException e) {
            resp.setStatus(400);
            objectMapper.writeValue(resp.getWriter(), ApiResponse.builder().ok(false).error(e.getMessage()).build());
        } catch (Exception e) {
            getServletContext().log("Ошибка добавления комментария", e);
            resp.setStatus(500);
            objectMapper.writeValue(resp.getWriter(), ApiResponse.builder().ok(false).error("Не удалось сохранить комментарий.").build());
        }
    }
}
