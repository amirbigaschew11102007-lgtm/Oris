package com.example.repository.impl;

import com.example.models.Comment;
import com.example.repository.CommentsRepository;
import jakarta.servlet.http.Part;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentsRepositoryJdbcImpl implements CommentsRepository {
    private final DataSource dataSource;

    private static final String SQL_findCommentsForThisPlace = "SELECT c.id, c.photo_id, c.user_id, c.content, c.attachment_path, " +
            "c.recomment_id, c.created_at, u.username FROM comments c " +
            "JOIN users u ON u.id = c.user_id " +
            "WHERE c.photo_id = ? ORDER BY c.created_at ASC";
    private static final String SQL_CREATE_COMMENTS = "INSERT INTO comments(photo_id, user_id, content, attachment_path, recomment_id) " +
            "VALUES (?,?,?,?,?) RETURNING id";

    public CommentsRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public long create(Comment comment) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_COMMENTS)) {
            preparedStatement.setLong(1, comment.getPhoto_id());
            preparedStatement.setLong(2, comment.getUser_id());
            preparedStatement.setString(3, comment.getContent());
            preparedStatement.setString(4, comment.getAttachment_path());
            if (comment.getRecomment_id() == null) {
                preparedStatement.setNull(5, Types.BIGINT);
            } else {
                preparedStatement.setLong(5, comment.getRecomment_id());
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("id");
                }
            }
        }
        throw new SQLException("PostgreSQL не вернул ID комментария.");
    }

    @Override
    public List<Comment> findByPhotoId(long photoId) throws SQLException {
        List<Comment> comments = new ArrayList<>();

        try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_findCommentsForThisPlace)){
            preparedStatement.setLong(1,photoId);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    comments.add(mapRow(resultSet));
                }
            }
        }
        return comments;
    }

    private Comment mapRow(ResultSet resultSet) throws SQLException {
        Timestamp createdAt = resultSet.getTimestamp("created_at");

        return Comment.builder()
                .id(resultSet.getLong("id"))
                .photo_id(resultSet.getLong("photo_id"))
                .user_id(resultSet.getLong("user_id"))
                .content(resultSet.getString("content"))
                .attachment_path(resultSet.getString("attachment_path"))
                .recomment_id(resultSet.getObject("recomment_id", Long.class))
                .createdAt(createdAt == null ? null : createdAt.toLocalDateTime())
                .build();
    }
}
