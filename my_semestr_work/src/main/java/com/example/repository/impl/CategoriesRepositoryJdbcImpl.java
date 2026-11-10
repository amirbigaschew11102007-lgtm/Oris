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
    private DataSource dataSource;

    private static final String SQL_AllPlacesForThisCategory = "SELECT id,name,description FROM categories " +
            "WHERE name=?";
    private static final String SQL_SAVE_Category = "INSERT INTO categories(name,description) " +
            "VALUES (?,?)";

    public CategoriesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Categories> AllPlacesForThisCategory(String category_name) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_AllPlacesForThisCategory)) {
            preparedStatement.setString(1, category_name);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Categories> categories = new ArrayList<>();
            while (resultSet.next()) {
                Categories category = Categories.builder()
                        .nameCategory(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .build();
                categories.add(category);
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Categories entity) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_Category);
        preparedStatement.setString(1, entity.getNameCategory());
        preparedStatement.setString(2, entity.getDescription());
        preparedStatement.executeUpdate();
    }
}
