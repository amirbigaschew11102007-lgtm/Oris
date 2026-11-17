package com.example.repository.impl;

import com.example.models.Categories;
import com.example.repository.CategoriesRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriesRepositoryJdbcImpl implements CategoriesRepository {
    private final DataSource dataSource;

    private static final String SQL_FIND_ALL = "SELECT id,name,description FROM categories " +
            "ORDER BY name";
    private static final String SQL_SAVE_Category = "INSERT INTO categories(name) " +
            "VALUES (?) RETURNING id";

    public CategoriesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Categories> findAll() throws SQLException {
        List<Categories> categories = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                categories.add(mapRow(resultSet));
            }

        }
        return categories;
    }

    @Override
    public long save(String entity) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_Category)) {
            preparedStatement.setString(1, entity);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                }
            }
        }
        throw new SQLException("PostgreSQL не вернул ID новой категории.");
    }

    private Categories mapRow(ResultSet resultSet) throws SQLException {
        return Categories.builder()
                .id(resultSet.getLong("id"))
                .nameCategory(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .build();
    }
}