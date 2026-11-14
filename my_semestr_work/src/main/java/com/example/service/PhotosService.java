package com.example.service;

import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.SQLException;

public interface PhotosService {
    long add(long placeId, long userId, Part part) throws SQLException, IOException;
}
