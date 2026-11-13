package com.example.repository.impl;

import com.example.repository.FavoritesRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class FavoritesRepositoryJdbcImpl implements FavoritesRepository {
    private final DataSource dataSource;

    private static final String SQL_EXISTS = "SELECT 1 FROM favorites WHERE user_id = ? AND place_id = ?";
    private static final String SQL_ADD = "INSERT INTO favorites (user_id, place_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
    private static final String SQL_REMOVE = "DELETE FROM favorites WHERE user_id = ? AND place_id = ?";
    private static final String SQL_FIND_PLACE_IDS = "SELECT place_id FROM favorites WHERE user_id = ?";

    public FavoritesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean exists(long userId, long placeId) throws SQLException {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_EXISTS)){
            preparedStatement.setLong(1,userId);
            preparedStatement.setLong(2,placeId);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                return resultSet.next();
            }
        }
    }

    @Override
    public void add(long userId, long placeId) throws SQLException {
        try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD)){
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, placeId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void remove(long userId, long placeId) throws SQLException {
        try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_REMOVE)){
            preparedStatement.setLong(1,userId);
            preparedStatement.setLong(2,placeId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Set<Long> findPlaceIds(long userId) throws SQLException {
        Set<Long> placeId = new HashSet<>();
        try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_PLACE_IDS)){
            preparedStatement.setLong(1,userId);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    placeId.add(resultSet.getLong("id"));
                }
            }
        }
        return placeId;
    }
}
