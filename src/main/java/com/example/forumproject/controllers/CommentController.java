package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.mappers.CommentMapper;
import com.example.forumproject.mappers.PostMapper;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.CommentDto;
import com.example.forumproject.models.dtos.CommentDtoOut;
import com.example.forumproject.services.CommentService;
import com.example.forumproject.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper, AuthenticationHelper authenticationHelper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<CommentDtoOut> getAllComments(@PathVariable int postId) {
        try {
            return commentService.getAll(postId)
                    .stream()
                    .map(commentMapper::objectToDto)
                    .collect(Collectors.toList());

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public CommentDtoOut getById(@PathVariable int postId, @PathVariable int id) {
        try {
            Comment comment = commentService.getById(postId, id);
            return commentMapper.objectToDto(comment);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException
                    (HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Comment createComment(@RequestHeader HttpHeaders headers, @PathVariable int postId,
                                 @Valid @RequestBody CommentDto commentDTO) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentMapper.dtoToObject(commentDTO, user);
            commentService.create(postId, comment, user);
            return comment;
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public Comment updateComment(@RequestHeader HttpHeaders headers, @PathVariable int postId,
                                 @PathVariable int id, @Valid @RequestBody CommentDto commentDTO) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentMapper.updateDtoToObject(commentDTO, postId, id);
            commentService.update(postId, comment, user);
           return comment;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException u) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, u.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int postId, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            commentService.delete(postId, id, user);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }
}




