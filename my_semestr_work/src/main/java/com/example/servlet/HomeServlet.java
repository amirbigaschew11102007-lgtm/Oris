package com.example.servlet;

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
import java.util.List;
import java.util.Set;


@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private PlacesService placesService;
    private FavoritesService favoritesService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        placesService = (PlacesService) config.getServletContext().getAttribute("places");
        favoritesService = (FavoritesService) config.getServletContext().getAttribute("favoritesService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            SessionUser user = Web.user(req);
            Long cityId = user ==null ? null:user.getCityId();

            List<Places> places = placesService.feed(cityId);
            Set<Long> favoritesIds = user == null ? Set.of() : favoritesService.ids(user.getId());
            for(Places place:places){
                place.setFavorite(favoritesIds.contains(place.getId()));
            }

            req.setAttribute("places", places);
            req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req,resp);
        } catch (SQLException e) {
            throw new ServletException("Не удалось загрузить ленту ScoutX.", e);
        }
    }
}
