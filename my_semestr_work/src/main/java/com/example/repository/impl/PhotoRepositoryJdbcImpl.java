package com.example.repository.impl;

import com.example.models.Photos;
import com.example.repository.PhotoRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhotoRepositoryJdbcImpl implements PhotoRepository {
    private final DataSource dataSource;
    private static final String SELECT_PHOTO =
            "SELECT p.id, p.place_id, p.uploaded_by, p.original_file_path, p.storage_file_path, " +
                    "p.created_at, u.username AS uploader_name FROM photos p " +
                    "JOIN users u ON u.id = p.uploaded_by ";

    private final static String SQL_CREATE = "INSERT INTO photos (place_id, uploaded_by, original_file_path, storage_file_path) " +
            "VALUES (?, ?, ?, ?) RETURNING id";
    private final static String SQL_FIND_BY_ID = SELECT_PHOTO + "WHERE p.place_id = ? ORDER BY p.created_at DESC";
    private final static String SQL_FIND_BY_PLACEID = SELECT_PHOTO + "WHERE p.id = ?";


    public PhotoRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public long create(Photos photo) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE)) {
            preparedStatement.setLong(1, photo.getPlace_id());
            preparedStatement.setLong(2, photo.getAuthor_id());
            preparedStatement.setString(3, photo.getOriginal_file_path());
            preparedStatement.setString(4, photo.getStorage_file_path());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                }
            }
        }
        throw new SQLException("PostgreSQL не вернул ID фотографии.");
    }

    @Override
    public List<Photos> findByPlaceId(long placeId) throws SQLException {
        List<Photos> photos = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_PLACEID)) {
            preparedStatement.setLong(1, placeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    photos.add(mapRow(resultSet));
                }
            }
        }
        return photos;
    }

    @Override
    public Optional<Photos> findById(long photoId) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            preparedStatement.setLong(1, photoId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Optional.of(mapRow(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    private Photos mapRow(ResultSet resultSet) throws SQLException {
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        return Photos.builder()
                .id(resultSet.getLong("id"))
                .place_id(resultSet.getLong("place_id"))
                .author_id(resultSet.getLong("uploaded_by"))
                .original_file_path(resultSet.getString("original_file_path"))
                .storage_file_path(resultSet.getString("storage_file_path"))
                .createdAt(createdAt == null ? null : createdAt.toLocalDateTime())
                .build();
    }
}
