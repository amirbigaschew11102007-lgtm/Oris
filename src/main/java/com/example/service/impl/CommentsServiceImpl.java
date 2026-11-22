package com.example.service.impl;

import com.example.dto.UploadedFile;
import com.example.models.Comment;
import com.example.models.Photos;
import com.example.models.Places;
import com.example.repository.CommentsRepository;
import com.example.repository.PhotoRepository;
import com.example.repository.PlaceRepository;
import com.example.service.CommentsService;
import com.example.service.FileStorageService;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.SQLException;

public class CommentsServiceImpl implements CommentsService {
    private final CommentsRepository commentsRepository;
    private final PlaceRepository placeRepository;
    private final PhotoRepository photoRepository;
    private final FileStorageService fileStorageService;

    public CommentsServiceImpl(CommentsRepository commentsRepository, PlaceRepository placeRepository, PhotoRepository photoRepository, FileStorageService fileStorageService) {
        this.commentsRepository = commentsRepository;
        this.placeRepository = placeRepository;
        this.photoRepository = photoRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Comment add(long photoId, long userId, String content, UploadedFile attachment) throws SQLException, IOException {
        Photos photos = photoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("Фотография не найдена."));
        Places places = placeRepository.findById(photos.getPlace_id())
                .orElseThrow(() -> new IllegalArgumentException("Место не найдено."));

        if(content ==null || content.trim().isEmpty()){
            throw new IllegalArgumentException("Напиши комментарий.");
        }
        if(content.length()>2000){
            throw new IllegalArgumentException("Комментарий слишком длинный (максимум 2000 символов).");
        }

        String attachmentPath = fileStorageService.saveImage(attachment);
        Comment comment = Comment.builder()
                .photo_id(photoId)
                .user_id(userId)
                .content(content)
                .attachment_path(attachmentPath)
                .build();

        try{
            long commentId = commentsRepository.create(comment);
            comment.setId(commentId);
            return comment;
        } catch (SQLException e) {
            // Удаляем файл, если не удалось сохранить комментарий в базе данных.
            fileStorageService.deleteQuietly(attachmentPath);
            throw e;
        }
    }
}
