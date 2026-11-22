package com.example.repository;

import com.example.models.Places;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PlaceRepository{
    Optional<Places> findById(long id) throws SQLException;
    List<Places> findFeed(Long cityId) throws SQLException;
    List<Places> findAllForAdmin() throws SQLException;
    List<Places> findFavorites(long userId) throws SQLException;
    long create(Places place) throws SQLException;
    void update(Places place) throws SQLException;
    void delete(long placeId) throws SQLException;
}
