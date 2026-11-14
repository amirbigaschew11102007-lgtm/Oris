package com.example.service;

import com.example.models.Comment;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.SQLException;

public interface CommentsService {
    Comment add(long photoId, long userId, String content, Part attachment)
            throws SQLException, IOException;
}
