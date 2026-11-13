package com.example.service.impl;

import com.example.models.Places;
import com.example.repository.CommentsRepository;
import com.example.repository.PhotoRepository;
import com.example.repository.PlaceRepository;
import com.example.service.PlacesService;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PlacesServiceImpl implements PlacesService {

//    private final PlaceRepository placeRepository;
//    private final PhotoRepository photoRepository;
//    private final CommentsRepository commentsRepository;
//    private final FileStorageService fileStorageService;

    @Override
    public Places findById(long id) throws SQLException {
        return null;
    }

    @Override
    public List<Places> feed(Long cityId) throws SQLException {
        return List.of();
    }

    @Override
    public List<Places> allForAdmin() throws SQLException {
        return List.of();
    }

    @Override
    public List<Places> favorites(long userId) throws SQLException {
        return List.of();
    }

    @Override
    public Places details(long id) throws SQLException {
        return null;
    }

    @Override
    public long create(Places Places, Part firstPhoto) throws SQLException, IOException {
        return 0;
    }

    @Override
    public void update(Places input, long currentUserId, boolean admin) throws SQLException {

    }

    @Override
    public void delete(long id, long currentUserId, boolean admin) throws SQLException {

    }
}
