package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.mappers.CommentMapper;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.dtos.commentDtos.CommentInDto;
import com.example.forumproject.models.dtos.commentDtos.CommentOutDto;
import com.example.forumproject.services.commentService.CommentService;
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

    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @GetMapping("/comments")
    public List<CommentOutDto> getAllComments(@PathVariable Long postId) {
        try {
            return commentService.getAll(postId)
                    .stream()
                    .map(commentMapper::commentToCommentOutDto)
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/comments/{commentId}")
    public CommentOutDto getById(@PathVariable Long postId,
                                 @PathVariable Long commentId) {
        try {
            Comment comment = commentService.getById(postId, commentId);
            return commentMapper.commentToCommentOutDto(comment);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/comments")
    public CommentOutDto createComment(@PathVariable Long postId,
                                       @Valid @RequestBody CommentInDto commentDTO) {
        try {
            Comment comment = commentMapper.CommentInDtoToObject(commentDTO);
            commentService.create(postId, comment);
            return commentMapper.commentToCommentOutDto(comment);
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/comments/{commentId}")
    public CommentOutDto updateComment(@PathVariable Long postId,
                                       @PathVariable Long commentId,
                                       @Valid @RequestBody CommentInDto commentDTO) {
        try {
            Comment comment = commentMapper.updateDtoToObject(commentDTO, postId, commentId);
            commentService.update(postId, comment);
            return commentMapper.commentToCommentOutDto(comment);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException u) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, u.getMessage());
        }
    }

    @DeleteMapping("/comments/{commentId}")
    public void delete(@PathVariable Long postId, @PathVariable Long commentId) {
        try {
            commentService.delete(postId, commentId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}