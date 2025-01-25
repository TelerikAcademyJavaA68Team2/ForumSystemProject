package com.example.forumproject.services;

import com.example.forumproject.models.Comment;
import com.example.forumproject.models.User;

import java.util.List;

public interface CommentService {

    List<Comment> getAll(int postId);

    Comment getById(int postId, int id);

    List<Comment> getCommentsByAuthor(int postId);

    void create(int postId, Comment comment, User user);

    void update(int postId, Comment comment, User user);

    void delete(int postId, int id, User user);

}
