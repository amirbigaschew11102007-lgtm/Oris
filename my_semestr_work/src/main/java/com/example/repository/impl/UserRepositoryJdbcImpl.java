package com.example.repository.impl;

import com.example.enums.Role;
import com.example.models.User;
import com.example.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository {
    private final DataSource dataBase;

    private static final String SQL_SELECT =
            "SELECT u.id, u.username, u.email, u.password_hash, u.city_id, " +
                    "u.role, u.avatar_path, c.name AS city_name " +
                    "FROM users u LEFT JOIN cities c ON c.id = u.city_id ";

    private static final String SQL_FIND_BY_ID = SQL_SELECT + "WHERE u.id = ?";
    private static final String SQL_FIND_BY_LOGIN = SQL_SELECT +
            "WHERE LOWER(u.email) = LOWER(?) OR LOWER(u.username) = LOWER(?)";

    private static final String SQL_EXISTS =
            "SELECT 1 FROM users WHERE LOWER(username) = LOWER(?) OR LOWER(email) = LOWER(?) LIMIT 1";

    private static final String SQL_CREATE =
            "INSERT INTO users (username, email, password_hash, city_id) VALUES (?, ?, ?, ?) RETURNING id";

    private static final String SQL_FIND_ALL = SQL_SELECT + "ORDER BY u.id DESC";


    public UserRepositoryJdbcImpl(DataSource dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public Optional<User> findById(long id) throws SQLException {
        try (Connection connection = dataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) throws SQLException {
        try (Connection connection = dataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByUsernameOrEmail(String name, String email) throws SQLException {
        try (Connection connection = dataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_EXISTS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public long create(User user) throws SQLException {
        try (Connection connection = dataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            if (user.getCity_id() == null) {
                preparedStatement.setNull(4, Types.BIGINT);
            } else {
                preparedStatement.setLong(4, user.getCity_id());
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                }
            }
        }
        throw new SQLException("PostgreSQL не вернул ID нового пользователя.");
    }

    @Override
    public List<User> findAll() {
        try (Connection connection = dataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(mapRow(resultSet));
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private User mapRow(ResultSet resultSet) throws SQLException {
        long cityIdValue = resultSet.getLong("city_id");
        Long cityId = resultSet.wasNull() ? null : cityIdValue;

        return User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("username"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password_hash"))
                .city_id(cityId)
                .avatarPath(resultSet.getString("avatar_path"))
                .role(Role.valueOf(resultSet.getString("role")))
                .build();
    }


//    @Override
//    public long create(User entity) throws SQLException {
//        Connection connection = dataBase.getConnection();
//        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_INTO_ACCOUNT);
//        preparedStatement.setString(1, entity.getName());
//        preparedStatement.setString(2, entity.getEmail());
//        preparedStatement.setString(3, entity.getPassword());
//        preparedStatement.executeUpdate();
//        return entity.getId();
//    }}
}
