package com.example.service;

import com.example.dto.AddPlaceForm;
import com.example.models.Places;
import com.example.repository.PlaceRepository;

import java.sql.SQLException;

public class AddPlaceServiceImpl implements AddPlaceService{
    private PlaceRepository placeRepository;

    public AddPlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public void AddPlaceService(PlaceRepository placeRepository){
        this.placeRepository = placeRepository;
    }

    @Override
    public void addPlace(AddPlaceForm form) throws SQLException {
        Places places = Places.builder()
                .title(form.getTitle())
                .description(form.getDescription())
                .streetAddress(form.getStreet_address())
                .build();
        placeRepository.save(places);
    }
}
