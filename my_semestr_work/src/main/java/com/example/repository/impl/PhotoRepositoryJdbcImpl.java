package com.example.repository.impl;

import com.example.models.Photos;
import com.example.repository.PhotoRepository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PhotoRepositoryJdbcImpl implements PhotoRepository {
    private final DataSource dataSource;

    private final static String SQL_CREATE="";
    private final static String SQL_FIND_BY_ID="";
    private final static String SQL_FIND_BY_PLACEID="";

    public PhotoRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public long create(Photos photo) throws SQLException {
        return 0;
    }

    @Override
    public List<Photos> findByPlaceId(long placeId) throws SQLException {
        return List.of();
    }

    @Override
    public Optional<Photos> findById(long photoId) throws SQLException {
        return Optional.empty();
    }
}
