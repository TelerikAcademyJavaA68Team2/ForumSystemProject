package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.mappers.PostMapper;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.postDtos.PostInDto;
import com.example.forumproject.models.dtos.postDtos.PostOutDto;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.reactionService.ReactionService;
import com.example.forumproject.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post Management", description = "API for managing forum posts")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;
    private final ReactionService reactionService;

    @Autowired
    public PostController(PostService postService, PostMapper postMapper,
                          UserService userService, ReactionService reactionService) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.reactionService = reactionService;
        this.userService = userService;
    }

    @Operation(
            description = "Get all posts with optional filters by title, content, tags, likes, and sorting options",
            summary = "Retrieve all posts",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved posts")
            }
    )
    @GetMapping
    public List<PostOutDto> getAllPosts(@RequestParam(required = false) String title,
                                        @RequestParam(required = false) String content,
                                        @RequestParam(required = false) String tags,
                                        @RequestParam(required = false) Long minLikes,
                                        @RequestParam(required = false) Long maxLikes,
                                        @RequestParam(required = false) String orderBy,
                                        @RequestParam(required = false) String orderType) {
        PostFilterOptions filterOptions = new PostFilterOptions(title, content, tags,
                minLikes, maxLikes, orderBy, orderType, null);
        try {
            List<Post> inPosts = postService.getAll(filterOptions);
            return inPosts.stream().map(postMapper::postToPostOutDto).toList();
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(
            description = "Retrieve a post by its ID",
            summary = "Get post by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved post"),
                    @ApiResponse(responseCode = "404", description = "Post not found")
            }
    )
    @GetMapping("/{postId}")
    public PostOutDto getPostById(@PathVariable Long postId) {
        try {
            Post post = postService.getById(postId);
            return postMapper.postToPostOutDto(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException
                    (HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            description = "Like or remove like from a post",
            summary = "Like a post",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully liked or unliked the post"),
                    @ApiResponse(responseCode = "404", description = "Post or user not found"),
                    @ApiResponse(responseCode = "400", description = "Duplicate like action")
            }
    )
    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId) {
        try {
            User user = userService.getAuthenticatedUser();
            Post post = postService.getById(postId);
            String message = reactionService.save(post, user, true)
                    ? "Post LIKED successfully!" : "LIKE removed successfully!";
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            description = "Dislike or remove dislike from a post",
            summary = "Dislike a post",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully disliked or removed dislike"),
                    @ApiResponse(responseCode = "404", description = "Post or user not found"),
                    // @ApiResponse(responseCode = "400", description = "Duplicate dislike action")
            }
    )
    @PostMapping("/{postId}/dislike")
    public ResponseEntity<String> dislikePost(@PathVariable Long postId) {
        try {
            User user = userService.getAuthenticatedUser();
            Post post = postService.getById(postId);
            String message = reactionService.save(post, user, false)
                    ? "Post DISLIKED successfully!" : "DISLIKE removed successfully!";
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            description = "Create a new post",
            summary = "Create a post",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created post"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized to create post"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PostMapping
    public PostOutDto create(@Valid @RequestBody PostInDto postDto) { //todo maby otherDto with no id in? updatedto?
        try {
            User user = userService.getAuthenticatedUser();
            Post postToCreate = postMapper.createPostFromDto(postDto, user);
            postService.create(postToCreate, user);
            return postMapper.postToPostOutDto(postToCreate);
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            description = "Update an existing post",
            summary = "Update a post",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated post"),
                    @ApiResponse(responseCode = "404", description = "Post or user not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized to update post"),
                    @ApiResponse(responseCode = "409", description = "Duplicate entity conflict")
            }
    )
    @PutMapping("/{postId}")
    public PostOutDto update(@PathVariable Long postId, @Valid @RequestBody PostInDto postDto) {
        try {
            User user = userService.getAuthenticatedUser();
            Post postToUpdate = postMapper.postInDtoToPost(postDto, postId);
            postService.update(postToUpdate, user);
            Post updatedPost = postService.getById(postId);
            return postMapper.postToPostOutDto(updatedPost);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            description = "Delete a post by ID",
            summary = "Delete a post",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully deleted post"),
                    @ApiResponse(responseCode = "404", description = "Post or user not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized to delete post")
            }
    )
    @DeleteMapping("/{postId}")
    public void delete(@PathVariable Long postId) {
        try {
            User user = userService.getAuthenticatedUser();
            postService.delete(postId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }
}