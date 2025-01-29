package com.example.forumproject.services.commentService;

import com.example.forumproject.models.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAllComments(Long postId);

    List<Comment> getAllCommentsByPostId(Long postId);

    Comment getById(Long postId, Long id);

    void create(Long postId, Comment comment);

    void update(Long postId, Comment comment);

    void delete(Long postId, Long id);

}
