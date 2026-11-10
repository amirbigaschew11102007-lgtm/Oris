package com.example.service;

import com.example.dto.AddPlaceForm;
import java.sql.SQLException;

public interface AddPlaceService {
    void addPlace (AddPlaceForm form) throws SQLException;

}
