package com.example.repository.impl;

import com.example.models.Categories;
import com.example.models.Comment;
import com.example.repository.CommentsRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentsRepositoryJdbcImpl implements CommentsRepository {
    private DataSource dataSource;

    private static final String SQL_findCommentsForThisPlace="SELECT c.* FROM comments c\n" +
            "JOIN photos p ON c.photo_id = p.id\n" +
            "WHERE c.photo_id = ?";
    private static final String SQL_SAVE_COMMENTS = "INSERT INTO comments(photo_id, user_id, content, attachment_path, recomment_id) " +
            "VALUES (?,?,?,?,?)";

    public CommentsRepositoryJdbcImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public List<Comment> findCommentsForThisPlace(Long place_id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_findCommentsForThisPlace)){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Comment> comments = new ArrayList<>();
            while (resultSet.next()){
                Comment comment = Comment.builder()
                        .photo_id(resultSet.getLong("photo_id"))
                        .user_id(resultSet.getLong("user_id"))
                        .content(resultSet.getString("content"))
                        .attachment_path(resultSet.getString("attachment_path"))
                        .recomment_id(resultSet.getLong("recomment_id"))
                        .build();
                comments.add(comment);
            }
            return comments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Comment entity) throws SQLException {
        Connection connection=dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_COMMENTS);
        preparedStatement.setLong(1,entity.getPhoto_id());
        preparedStatement.setLong(2,entity.getUser_id());
        preparedStatement.setString(3,entity.getContent());
        preparedStatement.setString(4,entity.getAttachment_path());
        preparedStatement.setLong(5,entity.getRecomment_id());
        preparedStatement.executeUpdate();
    }
}
