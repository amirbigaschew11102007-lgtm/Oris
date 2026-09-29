package com.example;

import com.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main{

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/classwork_Oris";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "redemption";

    public static void main(String[] args) throws SQLException {
        addSixUsers();

        System.out.println("Пользователи старше 20 лет:");

        findUsersOlderThan(20);

    }
    public static void addSixUsers() {
        String sql = """
        INSERT INTO users (name, age, email)
        VALUES
            ('Ivan', 19, 'ivan@gmail.com'),
            ('Anna', 21, 'anna@gmail.com'),
            ('Petr', 18, 'petr@gmail.com'),
            ('Maria', 25, 'maria@gmail.com'),
            ('Alexey', 20, 'alexey@gmail.com'),
            ('Elena', 23, 'elena@gmail.com')
        """;

        try (Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
             Statement statement = connection.createStatement()) {// WHAT???

            int count = statement.executeUpdate(sql);
            System.out.println("Добавлено пользователей: " + count);

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении пользователей");
            e.printStackTrace();
        }
    }

    public static void findUsersOlderThan(int age) {
        List<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT id,name,age, email " +
                     "FROM users " +
                     "WHERE age > " + age)) {

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email")
                );

                users.add(user);
            }

            for (User user : users) {
                System.out.println(user);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении пользователей");
            e.printStackTrace();
        }
    }
}
