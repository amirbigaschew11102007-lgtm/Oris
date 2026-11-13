package com.example.repository;

import com.example.models.Cities;

import java.sql.SQLException;
import java.util.List;

public interface CitiesRepository {
    List<Cities> findAll() throws SQLException;
    boolean exists(long cityId) throws SQLException;
}
