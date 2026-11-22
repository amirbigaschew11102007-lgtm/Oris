package com.example.repository.impl;

import com.example.models.Cities;
import com.example.repository.CitiesRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CitiesRepositoryJdbcImpl implements CitiesRepository {
    private final DataSource dataSource;

    private static final String SQL_FIND_ALL =
            "SELECT id, name, country FROM cities ORDER BY country, name";
    private static final String SQL_EXISTS =
            "SELECT 1 FROM cities WHERE id = ?";

    public CitiesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Cities> findAll() throws SQLException {
        List<Cities> cities = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                cities.add(mapRow(resultSet));
            }
        }
        return cities;
    }

    @Override
    public boolean exists(long cityId) throws SQLException {
        try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_EXISTS)){
            preparedStatement.setLong(1,cityId);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                return resultSet.next();
            }
        }
    }

    private Cities mapRow(ResultSet resultSet) throws SQLException {
        return Cities.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .country(resultSet.getString("country"))
                .build();
    }
}