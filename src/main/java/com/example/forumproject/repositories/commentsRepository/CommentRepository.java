package com.example.forumproject.repositories.commentsRepository;

import com.example.forumproject.models.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> getAllComments(Long postId);

    Comment getById(Long postId, Long id);

    List<Comment> getAllCommentsByPostIdForMapper(Long postId);

    void create(Long postId, Comment comment);

    void update(Long postId, Comment comment);

    void delete(Long postId, Long id);

}
