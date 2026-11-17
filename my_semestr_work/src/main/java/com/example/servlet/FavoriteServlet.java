package com.example.servlet;

import com.example.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.dto.SessionUser;
import com.example.models.Places;
import com.example.service.FavoritesService;
import com.example.service.PlacesService;
import com.example.util.Web;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/favorites")
public class FavoriteServlet extends HttpServlet {
    private PlacesService placesService;
    private FavoritesService favoritesService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        placesService = (PlacesService) config.getServletContext().getAttribute("placesService");
        favoritesService = (FavoritesService) config.getServletContext().getAttribute("favoritesService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            SessionUser user = Web.user(req);
            var places = placesService.favorites(user.getId());
            for(Places place: places){
                place.setFavorite(true);
            }
            req.setAttribute("places",places);
            req.getRequestDispatcher("/WEB-INF/views/favorites.jsp").forward(req, resp);
        }catch (Exception e) {
            throw new ServletException("Не удалось загрузить избранное.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            long placeId = Long.parseLong(req.getParameter("placeId"));
            SessionUser user = Web.user(req);
            boolean saved = favoritesService.toggle(user.getId(), placeId);
            objectMapper.writeValue(resp.getWriter(), ApiResponse.builder().ok(true).data(Map.of("saved", saved)).build());
        } catch (SQLException e) {
            resp.setStatus(400);
            objectMapper.writeValue(resp.getWriter(), ApiResponse.builder().ok(false).error("Не удалось изменить избранное.").build());
        }
    }
}
