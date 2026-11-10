package com.example.repository.impl;

import com.example.models.Places;
import com.example.repository.PlaceRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaceRepositoryJdbcImpl implements PlaceRepository {
    private DataSource dataBase;

    private static final String SQL_findAllOnYourCity="SELECT p.* FROM places p\n" +
            "JOIN users u ON p.city_id = u.city_id\n" +
            "WHERE u.city_id = ? " +
            "ORDER BY p.created_at";
    private static final String SQL_INSERT_INTO_PLACES =
            "INSERT INTO places(title, description,street_address,author_id,city_id,category_id,created_at)" +
            "VALUES (?,?,?,?,?,?,?)";

    public PlaceRepositoryJdbcImpl(DataSource dataBase) {
        this.dataBase=dataBase;
    }

    @Override
    public List<Places> findAllOnYourCity(Long userId) {
        try (Connection connection = dataBase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_findAllOnYourCity)){
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Places> postsForHomePage = new ArrayList<>();
            while(resultSet.next()){
                Places post = Places.builder()
                        .title(resultSet.getString("title"))
                        .description(resultSet.getString("description"))
                        .streetAddress(resultSet.getString("street_address"))
                        .authorId(resultSet.getLong("author_id"))
                        .cityId(resultSet.getLong("city_id"))
                        .categoryId(resultSet.getLong("category_id"))
                        .createdAt(resultSet.getTimestamp("created_at"))
                        .build();
                postsForHomePage.add(post);
            }
            return postsForHomePage;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Places entity) throws SQLException {
        Connection connection = dataBase.getConnection();
        PreparedStatement preparedStatement= connection.prepareStatement(SQL_INSERT_INTO_PLACES);
        preparedStatement.setString(1,entity.getTitle());
        preparedStatement.setString(2,entity.getDescription());
        preparedStatement.setString(3, entity.getStreetAddress());
        if (entity.getAuthorId() == null) {
            preparedStatement.setNull(4, Types.BIGINT);
        } else {
            preparedStatement.setLong(4, entity.getAuthorId());
        }

        if (entity.getCityId() == null) {
            preparedStatement.setNull(5, Types.BIGINT);
        } else {
            preparedStatement.setLong(5, entity.getCityId());
        }

        if (entity.getCategoryId() == null) {
            preparedStatement.setNull(6, Types.BIGINT);
        } else {
            preparedStatement.setLong(6, entity.getCategoryId());
        }
        preparedStatement.setTimestamp(7,entity.getCreatedAt());
        preparedStatement.executeUpdate();
    }
}
