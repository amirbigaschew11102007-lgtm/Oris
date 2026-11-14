package com.example.service.impl;

import com.example.models.Places;
import com.example.repository.FavoritesRepository;
import com.example.repository.PlaceRepository;
import com.example.service.FavoritesService;

import java.sql.SQLException;
import java.util.Set;

public class FavoritesServiceImpl implements FavoritesService {
    private final PlaceRepository placeRepository;
    private final FavoritesRepository favoritesRepository;

    public FavoritesServiceImpl(PlaceRepository placeRepository, FavoritesRepository favoritesRepository) {
        this.placeRepository = placeRepository;
        this.favoritesRepository = favoritesRepository;
    }


    @Override
    public boolean toggle(long userId, long placeId) throws SQLException {
        Places places = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Место не найдено."));

        if (favoritesRepository.exists(userId, placeId)) {
            favoritesRepository.remove(userId, placeId);
            return false;
        }
        favoritesRepository.add(userId, placeId);
        return true;
    }

    @Override
    public Set<Long> ids(long userId) throws SQLException {
        return favoritesRepository.findPlaceIds(userId);
    }
}
