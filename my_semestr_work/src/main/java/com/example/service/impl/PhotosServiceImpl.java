package com.example.service.impl;

import com.example.models.Photos;
import com.example.models.Places;
import com.example.repository.PhotoRepository;
import com.example.repository.PlaceRepository;
import com.example.service.FileStorageService;
import com.example.service.PhotosService;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.SQLException;

public class PhotosServiceImpl implements PhotosService {
    private final PhotoRepository photoRepository;
    private final PlaceRepository placeRepository;
    private final FileStorageService fileStorageService;

    public PhotosServiceImpl(PhotoRepository photoRepository, PlaceRepository placeRepository, FileStorageService fileStorageService) {
        this.photoRepository = photoRepository;
        this.placeRepository = placeRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public long add(long placeId, long userId, Part part) throws SQLException, IOException {
        Places places = placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Место не найдено."));

        String filename = fileStorageService.saveImage(part);
        if(filename==null){
            throw new IllegalArgumentException("Выбери фотографию.");
        }
        Photos photos = Photos.builder()
                .place_id(placeId)
                .author_id(userId)
                .original_file_path(fileStorageService.originalFileName(part))
                .storage_file_path(filename)
                .build();
        try{
            return photoRepository.create(photos);
        }catch (SQLException e) {
            // Если запись в БД не создалась, удаляем уже загруженный файл.
            fileStorageService.deleteQuietly(filename);
            throw e;
        }
    }
}
