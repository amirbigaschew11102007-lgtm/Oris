package com.example.service;

import com.example.models.Categories;
import com.example.models.Cities;

import java.sql.SQLException;
import java.util.List;

public interface CategoriesService {
    List<Cities> cities() throws SQLException;
    List<Categories> categories() throws SQLException;
}
