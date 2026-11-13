package com.example.repository;

import java.sql.SQLException;
import java.util.Set;

public interface FavoritesRepository {
    boolean exists(long userId, long placeId) throws SQLException;
    void add(long userId, long placeId) throws SQLException;
    void remove(long userId, long placeId) throws SQLException;
    Set<Long> findPlaceIds(long userId) throws SQLException;
}
