package com.example.service;

import com.example.dto.UploadedFile;
import com.example.models.Places;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface PlacesService {
    Places findById(long id) throws SQLException;
    List<Places> feed(Long cityId) throws SQLException;
    List<Places> allForAdmin() throws SQLException;
    List<Places> favorites(long userId) throws SQLException;
    Places details(long id) throws SQLException;
    long create(Places Places, UploadedFile firstPhoto) throws SQLException, IOException;
    void update(Places input, long currentUserId, boolean admin) throws SQLException;
    void delete(long id, long currentUserId, boolean admin) throws SQLException;
}
