package com.example.repository;

import com.example.models.Comment;

import java.util.List;

public interface CommentsRepository extends CrubRepository<Comment> {
    List<Comment> findCommentsForThisPlace(Long place_id);
}
