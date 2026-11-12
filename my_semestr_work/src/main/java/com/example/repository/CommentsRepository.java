package com.example.repository;

import com.example.models.Categories;
import com.example.models.Comment;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface CommentsRepository{
    long create(Comment comment) throws SQLException;
    List<Comment> findByPhotoId(long photoId) throws SQLException;
