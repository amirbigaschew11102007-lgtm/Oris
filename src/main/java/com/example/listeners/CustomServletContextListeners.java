package com.example.listeners;

import com.example.repository.*;
import com.example.repository.impl.*;
import com.example.service.*;
import com.example.service.impl.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebListener
public class CustomServletContextListeners implements ServletContextListener {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = CustomServletContextListeners.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in == null) throw new IllegalStateException("application.properties not found");
            PROPS.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        try {//позволяет обращаться к атрибутам других классов (сервлетам) во время работы приложения

            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setUrl(PROPS.getProperty("db.url"));
            dataSource.setUsername(PROPS.getProperty("db.user"));
            dataSource.setPassword(PROPS.getProperty("db.password"));

            UserRepository usersRepository = new UserRepositoryJdbcImpl(dataSource);
            CitiesRepository citiesRepository = new CitiesRepositoryJdbcImpl(dataSource);
            PlaceRepository placeRepository = new PlaceRepositoryJdbcImpl(dataSource);
            CategoriesRepository categoriesRepository = new CategoriesRepositoryJdbcImpl(dataSource);
            PhotoRepository photoRepository = new PhotoRepositoryJdbcImpl(dataSource);
            CommentsRepository commentsRepository = new CommentsRepositoryJdbcImpl(dataSource);
            FavoritesRepository favoritesRepository = new FavoritesRepositoryJdbcImpl(dataSource);

            FileStorageService fileStorageService = new FileStorageServiceImpl(
                    PROPS.getProperty("upload.dir", "./scoutx-uploads")
            );
            SignUpService signUpService = new SignUpServiceImpl(usersRepository);
            PlacesService placesService = new PlacesServiceImpl(
                    placeRepository, photoRepository, commentsRepository, fileStorageService
            );
            PhotosService photosService = new PhotosServiceImpl(
                    photoRepository, placeRepository, fileStorageService
            );
            CommentsService commentsService = new CommentsServiceImpl(
                    commentsRepository, placeRepository, photoRepository, fileStorageService
            );
            FavoritesService favoritesService = new FavoritesServiceImpl(placeRepository, favoritesRepository);
            AdminService adminService = new AdminServiceImpl(categoriesRepository, placeRepository);
            CategoriesService categoriesService = new CategoriesServiceImpl(categoriesRepository,citiesRepository);

            servletContext.setAttribute("userRepository", usersRepository);
            servletContext.setAttribute("rootPage", placeRepository);
            servletContext.setAttribute("categories", categoriesRepository);
            servletContext.setAttribute("cities", citiesRepository);
            servletContext.setAttribute("photosRepository", photoRepository);
            servletContext.setAttribute("commentsRepository", commentsRepository);
            servletContext.setAttribute("favoritesRepository", favoritesRepository);

            servletContext.setAttribute("signUp", signUpService);
            servletContext.setAttribute("placesService", placesService);
            servletContext.setAttribute("photosService", photosService);
            servletContext.setAttribute("commentsService", commentsService);
            servletContext.setAttribute("favoritesService", favoritesService);
            servletContext.setAttribute("adminService", adminService);
            servletContext.setAttribute("categoriesService",categoriesService);
            servletContext.setAttribute("fileStorageService", fileStorageService);

            servletContext.log("ScoutX: DataSource, Repository и Service созданы.");
        } catch (RuntimeException e) {
            servletContext.log("ScoutX: не удалось инициализировать приложение.", e);
            throw e;
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
