package com.example.repository;

import com.example.model.User;
import com.example.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public void addSixUsers() {
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

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            int count = statement.executeUpdate(sql);
            System.out.println("Добавлено пользователей: " + count);

        } catch (SQLException e) {
            System.out.println("Ошибка при добавлении пользователей");
            e.printStackTrace();
        }
    }

    public List<User> findUsersOlderThan(int age) {
        List<User> users = new ArrayList<>();

        String sql = "SELECT id,name,age, email " +
                "FROM users " +
                "WHERE age > " + age;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email")
                );

                users.add(user);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при получении пользователей");
            e.printStackTrace();
        }

        return users;
    }
}