package com.example.service.impl;

import com.example.models.Categories;
import com.example.models.Cities;
import com.example.repository.CategoriesRepository;
import com.example.repository.CitiesRepository;
import com.example.service.CategoriesService;

import java.sql.SQLException;
import java.util.List;

public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final CitiesRepository citiesRepository;

    public CategoriesServiceImpl(CategoriesRepository categoriesRepository, CitiesRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public List<Cities> cities() throws SQLException {
        return citiesRepository.findAll();
    }

    @Override
    public List<Categories> categories() throws SQLException {
        return categoriesRepository.findAll();
    }
}
