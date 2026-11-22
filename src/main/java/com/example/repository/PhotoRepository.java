package com.example.repository;

import com.example.models.Photos;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PhotoRepository {
    long create(Photos photo) throws SQLException;
    List<Photos> findByPlaceId(long placeId) throws SQLException;
    Optional<Photos> findById(long photoId) throws SQLException;

}
