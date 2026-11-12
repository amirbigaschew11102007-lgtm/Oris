package com.example.service;

import java.sql.SQLException;

public interface AdminService {
    void deletePlace(long id) throws SQLException;
    void addCategory(String name) throws SQLException;
}