package com.example.forumproject.services.contracts;

import com.example.forumproject.models.Comment;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.CommentDto;

import java.util.List;

public interface CommentService {

    List<Comment> getAll(int postId);

    List<CommentDto> getAllCommentDtos(int postId);

    List<Comment> getAllCommentsByPostId(int postId);

    Comment getById(int postId, int id);

    List<Comment> getCommentsByAuthor(int postId);

    void create(int postId, Comment comment, User user);

    void update(int postId, Comment comment, User user);

    void delete(int postId, int id, User user);

}
