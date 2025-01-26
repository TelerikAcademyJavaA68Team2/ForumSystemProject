package com.example.forumproject.repositories.contracts;

import com.example.forumproject.models.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> getAll(int postId);

    Comment getById(int postId, int id);

    List<Comment> getCommentsByAuthor(int postId, int userId);

    void create(int postId, Comment comment);

    void update(int postId, Comment comment);

    void delete(int postId, int id);

}
