package com.example.service.impl;

import com.example.repository.CategoriesRepository;
import com.example.repository.PlaceRepository;
import com.example.service.AdminService;

import java.sql.SQLException;

public class AdminServiceImpl implements AdminService {
    private final CategoriesRepository categoriesRepository;
    private final PlaceRepository placeRepository;

    public AdminServiceImpl(CategoriesRepository categoriesRepository, PlaceRepository placeRepository) {
        this.categoriesRepository = categoriesRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public void deletePlace(long id) throws SQLException {
        placeRepository.delete(id);
    }

    @Override
    public void addCategory(String name) throws SQLException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название категории должно содержать хоть что-то");
        }
        categoriesRepository.save(name.trim());
    }
}
