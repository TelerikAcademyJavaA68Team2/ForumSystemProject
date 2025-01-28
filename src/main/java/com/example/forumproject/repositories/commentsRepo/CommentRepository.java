package com.example.forumproject.repositories.commentsRepo;

import com.example.forumproject.models.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> getAll(Long postId);

    Comment getById(Long postId, Long id);

    List<Comment> getAllCommentsByPostId(Long postId);

    List<Comment> getCommentsByAuthor(Long postId, Long userId);

    void create(Long postId, Comment comment);

    void update(Long postId, Comment comment);

    void delete(Long postId, Long id);

}
