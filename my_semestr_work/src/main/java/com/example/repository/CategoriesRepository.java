package com.example.repository;

import com.example.models.Categories;

import java.util.List;

public interface CategoriesRepository extends CrubRepository<Categories> {
    List<Categories> AllPlacesForThisCategory(String category_name);
}
