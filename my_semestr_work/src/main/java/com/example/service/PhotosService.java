package com.example.service;

import com.example.dto.UploadedFile;

import java.io.IOException;
import java.sql.SQLException;

public interface PhotosService {
    long add(long placeId, long userId, UploadedFile uploadedFile) throws SQLException, IOException;
}
