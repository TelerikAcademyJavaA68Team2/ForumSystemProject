package com.example.forumproject.services.commentService;

import com.example.forumproject.models.Comment;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.commentDtos.CommentOutDto;

import java.util.List;

public interface CommentService {

    List<Comment> getAll(Long postId);

    List<CommentOutDto> getAllCommentDtos(Long postId);

    List<Comment> getAllCommentsByPostId(Long postId);

    Comment getById(Long postId, Long id);

    List<Comment> getCommentsByAuthor(Long postId);

    void create(Long postId, Comment comment, User user);

    void update(Long postId, Comment comment, User user);

    void delete(Long postId, Long id, User user);

}
