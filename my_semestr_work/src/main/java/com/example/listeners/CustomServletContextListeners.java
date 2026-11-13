package com.example.listeners;

import com.example.repository.CategoriesRepository;
import com.example.repository.PlaceRepository;
import com.example.repository.impl.CategoriesRepositoryJdbcImpl;
import com.example.repository.impl.PlaceRepositoryJdbcImpl;
import com.example.repository.UserRepository;
import com.example.repository.impl.UserRepositoryJdbcImpl;
import com.example.service.PlacesService;
import com.example.service.SignUpService;
import com.example.service.impl.PlacesServiceImpl;
import com.example.service.impl.SignUpServiceImpl;
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
        //позволяет обращаться к атрибутам других классов (сервлетам) во время работы приложения
        ServletContext servletContext = sce.getServletContext();


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
        PlaceRepository placeRepository = new PlaceRepositoryJdbcImpl(dataSource);
        SignUpService signUpService = new SignUpServiceImpl(usersRepository);
        CategoriesRepository categoriesRepository = new CategoriesRepositoryJdbcImpl(dataSource);
        PlacesService addPlaceService = new PlacesServiceImpl();

        servletContext.setAttribute("userRepository",usersRepository);
        servletContext.setAttribute("rootPage", placeRepository);
        servletContext.setAttribute("signUp",signUpService);
        servletContext.setAttribute("categories",categoriesRepository);
        servletContext.setAttribute("AddPlace",addPlaceService);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
