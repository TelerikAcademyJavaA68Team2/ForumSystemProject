package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.mappers.CommentMapper;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.CommentDto;
import com.example.forumproject.services.contracts.CommentService;
import com.example.forumproject.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts/{postId}")
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper, UserService userService) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.userService = userService;
    }

    @GetMapping("/comments")
    public List<CommentDto> getAllComments(@PathVariable int postId) {
        try {
            return commentService.getAll(postId)
                    .stream()
                    .map(commentMapper::commentToCommentDto)
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @GetMapping("/comments/{id}")
    public CommentDto getById(@PathVariable int postId, @PathVariable int id) {
        try {
            Comment comment = commentService.getById(postId, id);
            return commentMapper.commentToCommentDto(comment);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException
                    (HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/comments")
    public Comment createComment(@PathVariable int postId,
                                                                 @Valid @RequestBody CommentDto commentDTO) {
        try {
            User user = userService.getAuthenticatedUser();
            Comment comment = commentMapper.dtoToObject(commentDTO, user);
            commentService.create(postId, comment, user);
            return comment;
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PutMapping("/comments/{id}")
    public Comment updateComment(@PathVariable int postId,
                                 @PathVariable int id, @Valid @RequestBody CommentDto commentDTO) {
        try {
            User user = userService.getAuthenticatedUser();
            Comment comment = commentMapper.updateDtoToObject(commentDTO, postId, id);
            commentService.update(postId, comment, user);
           return comment;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException u) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, u.getMessage());
        }
    }

    @DeleteMapping("/comments/{id}")
    public void delete(@PathVariable int postId, @PathVariable int id) {
        try {
            User user = userService.getAuthenticatedUser();
            commentService.delete(postId, id, user);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }
}




