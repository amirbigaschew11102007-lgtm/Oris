package com.example.repository;

import java.sql.SQLException;

public interface CrubRepository<T> {

    void save(T entity) throws SQLException;
}

