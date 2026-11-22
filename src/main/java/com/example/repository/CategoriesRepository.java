package com.example.repository;

import com.example.models.Categories;

import java.sql.SQLException;
import java.util.List;

public interface CategoriesRepository{
    List<Categories> findAll() throws SQLException;
    long save(String entity) throws SQLException;
}
