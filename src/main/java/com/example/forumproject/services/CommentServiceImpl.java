package com.example.forumproject.services;

import com.example.forumproject.models.Comment;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.CommentRepository;
import com.example.forumproject.repositories.PostRepository;
import com.example.forumproject.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.forumproject.helpers.ValidationHelpers.validateUserIsAdminOrCommentAuthor;
import static com.example.forumproject.helpers.ValidationHelpers.validateUserIsNotBlocked;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Comment> getAll(int postId) {
        return commentRepository.getAll(postId);
    }

    @Override
    public Comment getById(int postId, int id) {
        return commentRepository.getById(postId, id);
    }

    @Override
    public List<Comment> getCommentsByAuthor(int postId) {
        User author = postRepository.getById(postId).getAuthor();
        return commentRepository.getCommentsByAuthor(postId, author.getId());
    }

    @Override
    public void create(int postId, Comment comment, User user) {
        validateUserIsNotBlocked(user);
        Post postToAddCommentTo = postRepository.getById(postId);
        comment.setAuthor(user);
        comment.setPost(postToAddCommentTo);
        commentRepository.create(postId, comment);
    }

    @Override
    public void update(int postId, Comment comment, User user) {
        validateUserIsNotBlocked(user);
        Comment commentToUpdate = commentRepository.getById(postId, comment.getId());
        validateUserIsAdminOrCommentAuthor(commentToUpdate, user);
        commentRepository.update(postId, comment);
    }

    @Override
    public void delete(int postId, int id, User user) {
        validateUserIsNotBlocked(user);
        Comment commentToDelete = commentRepository.getById(postId, id);
        validateUserIsAdminOrCommentAuthor(commentToDelete, user);
        commentRepository.delete(postId, id);
    }


}
