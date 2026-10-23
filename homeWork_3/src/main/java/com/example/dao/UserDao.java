package com.example.dao;

import com.example.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDao {

    private static final String SQL_GET_COUNT =
            "SELECT setting_value FROM settings WHERE setting_name = ?";
    private static final String SQL_saveUsers = """
            INSERT INTO users_3 (name, email,age,city)
            VALUES (?, ?, ?, ?)
            """;
    private static final String SQL_findByName = "SELECT * FROM users_3 WHERE name = ?";
    private static final String SQL_findByCity = "SELECT * FROM users_3 WHERE city = ?";
    private static final String SQL_findByPrice = "SELECT * FROM users_3 WHERE email = ?";


    private static final Properties PROPS = new Properties();
    static {
        try (InputStream in = UserDao.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (in == null) throw new IllegalStateException("application.properties not found");
            PROPS.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");//тк проблемы были с версиями (плагинов) или с lib директорией в самом tomcat
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC driver not found", e);
        }
        return DriverManager.getConnection(PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password"));
    }

    public int getUsersToAdd() throws SQLException {

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_COUNT)) {

            statement.setString(1, "users_to_add");

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("setting_value");
                }
            }
        }

        return 0;
    }

    public void saveUsers(User product) throws SQLException {

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_saveUsers)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getEmail());
            statement.setInt(3, product.getAge());
            statement.setString(4, product.getCity());

            statement.executeUpdate();
        }
    }

    public List<User> findByName(String name) throws SQLException {
        return findUsers(SQL_findByName, name);
    }

    public List<User> findByCity(String city) throws SQLException {
        return findUsers(SQL_findByCity, city);
    }

    public List<User> findByEmail(String email) throws SQLException {
        return findUsers(SQL_findByPrice, email);
    }

    private List<User> findUsers(String sql, String value) throws SQLException {
        List<User> products = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(new User(
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getInt("age"),
                            resultSet.getString("city")
                    ));
                }
            }
        }

        return products;
    }
}