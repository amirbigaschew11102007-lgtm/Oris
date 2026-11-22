package com.example.service.impl;

import com.example.dto.UploadedFile;
import com.example.models.Comment;
import com.example.models.Photos;
import com.example.models.Places;
import com.example.repository.CommentsRepository;
import com.example.repository.PhotoRepository;
import com.example.repository.PlaceRepository;
import com.example.service.FileStorageService;
import com.example.service.PlacesService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PlacesServiceImpl implements PlacesService {

    private final PlaceRepository placeRepository;
    private final PhotoRepository photoRepository;
    private final CommentsRepository commentsRepository;
    private final FileStorageService fileStorageService;

    public PlacesServiceImpl(PlaceRepository placeRepository, PhotoRepository photoRepository, CommentsRepository commentsRepository, FileStorageService fileStorageService) {
        this.placeRepository = placeRepository;
        this.photoRepository = photoRepository;
        this.commentsRepository = commentsRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Places findById(long id) throws SQLException {
        return placeRepository.findById(id).orElse(null);
        //orElse() иначе надо делать Optional<>
    }

    @Override
    public List<Places> feed(Long cityId) throws SQLException {
        return placeRepository.findFeed(cityId);
    }

    @Override
    public List<Places> allForAdmin() throws SQLException {
        return placeRepository.findAllForAdmin();
    }

    @Override
    public List<Places> favorites(long userId) throws SQLException {
        return placeRepository.findFavorites(userId);
    }

    @Override
    public Places details(long id) throws SQLException {
        Places place = placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Публикация не найдена."));

        List<Photos> photos = photoRepository.findByPlaceId(id);
        for (Photos photo: photos){
            List<Comment> comments = commentsRepository.findByPhotoId(photo.getId());
            photo.setComments(comments);
        }
        place.setPhotos(photos);
        return place;
    }

    @Override
    public long create(Places places, UploadedFile firstPhoto) throws SQLException, IOException {
        //тупо проверка на пустоту
        places.setTitle(required(places.getTitle(),"Укажите название места"));
        if (places.getTitle().length() > 150) {
            throw new IllegalArgumentException("Название слишком длинное (максимум 150 символов).");
        }
        if (firstPhoto == null || firstPhoto.getSize() == 0) {
            throw new IllegalArgumentException("Прикрепи хотя бы одну фотографию места.");
        }

        long placeId= placeRepository.create(places);
        String savedFileName = null;
        try {
            //именно сохранённое имя, а не имя ориг файла
            savedFileName = fileStorageService.saveImage(firstPhoto);

            Photos photo = Photos.builder()
                    .place_id(placeId)
                    .author_id(places.getAuthorId())
                    .original_file_path(fileStorageService.originalFileName(firstPhoto))
                    .storage_file_path(savedFileName)
                    .build();

            photoRepository.create(photo);
        } catch (SQLException | IOException | RuntimeException e) {
            // Если загрузка или запись фото не удалась, удаляем файл и запись места.
            fileStorageService.deleteQuietly(savedFileName);
            placeRepository.delete(placeId);
            throw e;
        }

        return placeId;
    }

    @Override
    public void update(Places input, long currentUserId, boolean admin) throws SQLException {
        Places existing = placeRepository.findById(input.getId())
                .orElseThrow(() -> new IllegalArgumentException("Публикация не найдена."));

        if (!admin && existing.getAuthorId() != currentUserId) {
            throw new SecurityException("Можно редактировать только собственные публикации.");
        }

        input.setTitle(required(input.getTitle(), "Укажите название места"));
        if(input.getTitle().length()>150){
            throw new IllegalArgumentException("Название слишком длинное.");
        }

        placeRepository.update(input);
    }

    @Override
    public void delete(long id, long currentUserId, boolean admin) throws SQLException {
        Places existing = placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Публикация не найдена."));

        if (!admin && existing.getAuthorId() != currentUserId){
            throw new SecurityException("Можно удалять только собственные публикации.");
        }

        placeRepository.delete(id);
    }

    private String required(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
