package com.example.servlet;

import com.example.dto.AddPlaceForm;
import com.example.dto.SignUpForm;
import com.example.service.AddPlaceService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/addPlace")
public class AddPlaceServlet extends HttpServlet {
    private AddPlaceService addPlaceService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        addPlaceService = (AddPlaceService) config.getServletContext().getAttribute("AddPlace");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/html/addPlace.html").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AddPlaceForm addPlaceForm = AddPlaceForm.builder()
                .title(req.getParameter("title"))
                .photo_id(Long.valueOf(req.getParameter("photo_id")))
                .description(req.getParameter("description"))
                .street_address(req.getParameter("street_address"))
                .build();
        try {
            addPlaceService.addPlace(addPlaceForm);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
