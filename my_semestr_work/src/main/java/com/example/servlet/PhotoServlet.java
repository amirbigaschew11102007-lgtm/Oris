package com.example.servlet;

import com.example.dto.SessionUser;
import com.example.service.PhotosService;
import com.example.util.Web;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/photos")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class PhotoServlet extends HttpServlet {
    private PhotosService photosService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        photosService = (PhotosService) config.getServletContext().getAttribute("photosService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            long placeId = Long.parseLong(req.getParameter("placeId"));
            SessionUser user = Web.user(req);
            photosService.add(placeId, user.getId(), req.getPart("photo"));
            resp.sendRedirect(req.getContextPath() + "/places/view?id=" + placeId);
        }catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Не удалось загрузить фотографию.", e);
        }
    }
}
