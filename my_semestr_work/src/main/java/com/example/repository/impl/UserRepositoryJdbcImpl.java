package com.example.repository.impl;

import com.example.models.Role;
import com.example.models.User;
import com.example.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryJdbcImpl implements UserRepository {
    private DataSource dataBase;

    private static final String SQL_SELECT_ALL_FROM_USERS =
            "SELECT id, username, email, city_id, role, avatar_path FROM USERS";
    private static final String SQL_INSERT_INTO_ACCOUNT="INSERT INTO users(username,email,password_hash)" +
            "VALUES (?,?,?)";

    public UserRepositoryJdbcImpl(DataSource dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public List findAll() {
        try (Connection connection = dataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_FROM_USERS)){
            ResultSet resultSet = preparedStatement.executeQuery();

            List<User> users = new ArrayList<>();
            while(resultSet.next()){
                User user = User.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("username"))
                        .email(resultSet.getString("email"))
                        .city_id(resultSet.getLong("city_id"))
                        .role(Role.valueOf(resultSet.getString("role")))
                        .role(resultSet.getString("avatar_path"))
                        .build();
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User entity) throws SQLException {
        Connection connection = dataBase.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_INTO_ACCOUNT);
        preparedStatement.setString(1,entity.getName());
        preparedStatement.setString(2,entity.getEmail());
        preparedStatement.setString(3,entity.getPassword());
        preparedStatement.executeUpdate();
    }
}
