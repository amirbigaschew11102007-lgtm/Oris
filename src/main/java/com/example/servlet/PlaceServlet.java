package com.example.servlet;

import com.example.dto.SessionUser;
import com.example.models.Places;
import com.example.repository.CategoriesRepository;
import com.example.repository.CitiesRepository;
import com.example.service.CategoriesService;
import com.example.service.PlacesService;
import com.example.util.MultipartFileReader;
import com.example.util.Web;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/places/*")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 7 * 1024 * 1024)
public class PlaceServlet extends HttpServlet {
    private PlacesService placesService;
    private CategoriesService categoriesService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        placesService = (PlacesService) config.getServletContext().getAttribute("placesService");
        categoriesService = (CategoriesService) config.getServletContext().getAttribute("categoriesService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        SessionUser user = Web.user(req);
        try{
            if("/new".equals(path)){
                prepareForm(req);
                req.getRequestDispatcher("/WEB-INF/views/place-form.jsp").forward(req,resp);
                return;//досрочно выходим
            }
            if("/view".equals(path)){
                long id = Long.parseLong(req.getParameter("id"));
                Places place = placesService.details(id);
                req.setAttribute("place",place);
                req.getRequestDispatcher("/WEB-INF/views/place.jsp").forward(req, resp);
                return;
            }
            if("/edit".equals(path)) {
                long id = Long.parseLong(req.getParameter("id"));
                Places place = placesService.details(id);
                if(place==null){
                    resp.sendError(404);
                    return;
                }
                if (user == null || (!user.isAdmin() && !user.getId().equals(place.getAuthorId()))) {
                    resp.sendError(403);
                    return;
                }
                req.setAttribute("place",place);
                prepareForm(req);
                req.getRequestDispatcher("/WEB-INF/views/place-form.jsp").forward(req, resp);
                return;
            }

            resp.sendError(404);
        }catch (NumberFormatException e) {
            resp.sendError(400, "Некорректный идентификатор.");
        } catch (IllegalArgumentException e) {
            resp.sendError(404, e.getMessage());
        } catch (Exception e) {
            throw new ServletException("Ошибка загрузки публикации.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        SessionUser user = Web.user(req);

        try{
            if("/create".equals(path)){
                Places place = new Places();
                place.setTitle(req.getParameter("title"));
                place.setDescription(req.getParameter("description"));
                place.setStreetAddress(req.getParameter("streetAddress"));
                place.setAuthorId(user.getId());
                place.setCityId(Long.parseLong(req.getParameter("cityId")));
                place.setCategoryId(Long.parseLong(req.getParameter("categoryId")));

                long id = placesService.create(place, MultipartFileReader.read(req.getPart("photo")));
                resp.sendRedirect(req.getContextPath() + "/places/view?id=" + id + "&created=1");
                return;
            }
            if ("/update".equals(path)) {
                Places place = new Places();
                place.setId(Long.parseLong(req.getParameter("id")));
                place.setTitle(req.getParameter("title"));
                place.setDescription(req.getParameter("description"));
                place.setStreetAddress(req.getParameter("streetAddress"));
                place.setCityId(Long.parseLong(req.getParameter("cityId")));
                place.setCategoryId(Long.parseLong(req.getParameter("categoryId")));

                placesService.update(place, user.getId(), user.isAdmin());
                resp.sendRedirect(req.getContextPath() + "/places/view?id=" + place.getId());
                return;
            }if ("/delete".equals(path)) {
                long id = Long.parseLong(req.getParameter("id"));
                placesService.delete(id, user.getId(), user.isAdmin());
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }
            resp.sendError(404);
        }catch (IllegalArgumentException | SecurityException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Ошибка при сохранении публикации.", e);
        }
    }

    private void prepareForm(HttpServletRequest request) throws Exception {
        request.setAttribute("cities", categoriesService.cities());
        request.setAttribute("categories", categoriesService.categories());
    }
}
