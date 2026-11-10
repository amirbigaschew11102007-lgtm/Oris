package com.example.servlet;

import com.example.repository.PlaceRepository;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("")
public class RootServlet extends HttpServlet {
    private PlaceRepository placeRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        placeRepository = (PlaceRepository) config.getServletContext().getAttribute("rootPage");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("AllPlacesOnYourCity", placeRepository.findAllOnYourCity(1L));
        req.getRequestDispatcher("WEB-INF/jsp/root.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
