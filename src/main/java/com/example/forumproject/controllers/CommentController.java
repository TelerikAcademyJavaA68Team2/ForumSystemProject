package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.mappers.CommentMapper;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.dtos.commentDtos.CommentInDto;
import com.example.forumproject.models.dtos.commentDtos.CommentOutDto;
import com.example.forumproject.services.commentService.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts/{postId}")
@Tag(name = "Comment Management", description = "Endpoints for managing comments on posts")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @Operation(
            summary = "Get all comments for a post",
            description = "Retrieve all comments associated with a specific post",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401")
            }
    )
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

    @Operation(
            summary = "Get a comment by ID",
            description = "Retrieve a specific comment by its ID within a post",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Comment not found", responseCode = "404")
            }
    )
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

    @Operation(
            summary = "Create a new comment",
            description = "Add a new comment to a specific post",
            responses = {
                    @ApiResponse(description = "Comment created successfully", responseCode = "201"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401")
            }
    )
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

    @Operation(
            summary = "Update a comment",
            description = "Update the content of an existing comment",
            responses = {
                    @ApiResponse(description = "Comment updated successfully", responseCode = "200"),
                    @ApiResponse(description = "Comment not found", responseCode = "404"),
                    @ApiResponse(description = "Conflict with existing data", responseCode = "409"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401")
            }
    )
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

    @Operation(
            summary = "Delete a comment",
            description = "Remove a specific comment from a post",
            responses = {
                    @ApiResponse(description = "Comment deleted successfully", responseCode = "204"),
                    @ApiResponse(description = "Comment not found", responseCode = "404"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401")
            }
    )
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