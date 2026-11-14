package com.example.service;

import java.sql.SQLException;
import java.util.Set;

public interface FavoritesService {
    boolean toggle(long userId, long placeId) throws SQLException;
    Set<Long> ids(long userId) throws SQLException;
}
