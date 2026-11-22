package com.example.repository.impl;

import com.example.models.Categories;
import com.example.models.Places;
import com.example.repository.PlaceRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlaceRepositoryJdbcImpl implements PlaceRepository {
    private final DataSource dataSource;

    private static final String SELECT_PLACE =
            "SELECT p.id, p.title, p.description, p.street_address, p.author_id, p.city_id, " +
                    "p.category_id, p.created_at, u.username AS author_name, " +
                    "c.name AS city_name, cat.name AS category_name, " +
                    "(SELECT ph.storage_file_path FROM photos ph WHERE ph.place_id = p.id " +
                    "ORDER BY ph.created_at DESC LIMIT 1) AS preview_photo " +
                    "FROM places p " +
                    "JOIN users u ON u.id = p.author_id " +
                    "JOIN cities c ON c.id = p.city_id " +
                    "JOIN categories cat ON cat.id = p.category_id ";
    private static final String SQL_FIND_BY_ID = SELECT_PLACE + "WHERE p.id = ?";

    // Если cityId == NULL, показываем общую ленту; иначе фильтруем по городу.
    private static final String SQL_FIND_FEED = SELECT_PLACE +
            "WHERE (? IS NULL OR p.city_id = ?) " +
            "ORDER BY COALESCE((SELECT MAX(ph2.created_at) FROM photos ph2 " +
            "WHERE ph2.place_id = p.id), p.created_at) DESC LIMIT 50";

    private static final String SQL_FIND_ALL_FOR_ADMIN = SELECT_PLACE + "ORDER BY p.created_at DESC";
    private static final String SQL_FIND_FAVORITES = SELECT_PLACE +
            "JOIN favorites f ON f.place_id = p.id " +
            "WHERE f.user_id = ? ORDER BY p.created_at DESC";
    private static final String SQL_CREATE =
            "INSERT INTO places (title, description, street_address, author_id, city_id, category_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
    private static final String SQL_UPDATE =
            "UPDATE places SET title = ?, description = ?, street_address = ?, city_id = ?, category_id = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM places WHERE id = ?";


    public PlaceRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Places> findById(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
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
    public List<Places> findFeed(Long cityId) throws SQLException {
        List<Places> places = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_FEED)) {
            if (cityId == null) {
                preparedStatement.setNull(1, Types.BIGINT);
                preparedStatement.setNull(2, Types.BIGINT);
            } else {
                preparedStatement.setLong(1, cityId);
                preparedStatement.setLong(2, cityId);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    places.add(mapRow(resultSet));
                }
            }
        }
        return places;
    }

    @Override
    public List<Places> findAllForAdmin() throws SQLException {
        List<Places> places = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_FOR_ADMIN);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                places.add(mapRow(resultSet));
            }
        }
        return places;
    }

    @Override
    public List<Places> findFavorites(long userId) throws SQLException {
        List<Places> places = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_FAVORITES)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    places.add(mapRow(resultSet));
                }
            }
        }
        return places;
    }

    @Override
    public long create(Places place) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE)) {
            preparedStatement.setString(1, place.getTitle());
            preparedStatement.setString(2, place.getDescription());
            preparedStatement.setString(3, place.getStreetAddress());
            preparedStatement.setLong(4, place.getAuthorId());
            preparedStatement.setLong(5, place.getCityId());
            preparedStatement.setLong(6, place.getCategoryId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                }
            }
        }
        throw new SQLException("PostgreSQL не вернул ID новой публикации.");
    }

    @Override
    public void update(Places place) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, place.getTitle());
            statement.setString(2, place.getDescription());
            statement.setString(3, place.getStreetAddress());
            statement.setLong(4, place.getCityId());
            statement.setLong(5, place.getCategoryId());
            statement.setLong(6, place.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(long placeId) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setLong(1, placeId);
            statement.executeUpdate();
        }
    }

    private Places mapRow(ResultSet resultSet) throws SQLException {
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        return Places.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .description(resultSet.getString("description"))
                .streetAddress(resultSet.getString("street_address"))
                .authorId(resultSet.getLong("author_id"))
                .cityId(resultSet.getLong("city_id"))
                .categoryId(resultSet.getLong("category_id"))
                .createdAt(createdAt == null ? null : createdAt.toLocalDateTime())
                .authorName(resultSet.getString("author_name"))
                .cityName(resultSet.getString("city_name"))
                .categoryName(resultSet.getString("category_name"))
                .previewPhotoPath(resultSet.getString("preview_photo"))
                .build();
    }
}