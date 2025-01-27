package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.mappers.PostMapper;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.PostInDto;
import com.example.forumproject.models.dtos.PostOutDto;
import com.example.forumproject.models.dtos.UpdatePostDto;
import com.example.forumproject.services.contracts.LikeService;
import com.example.forumproject.services.contracts.PostService;
import com.example.forumproject.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;
    private final LikeService likeService;

    @Autowired
    public PostController(PostService postService, PostMapper postMapper, UserService userService, LikeService likeService) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.likeService = likeService;
        this.userService = userService;
    }

    //ToDo Filtering !
    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postService.getAll();
    }

    @GetMapping("/posts/{id}")
    public PostOutDto getById(@PathVariable int id) {
        try {
            Post post = postService.getById(id);
            return postMapper.postToPostOutDto(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException
                    (HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/posts/{id}/like")
    public ResponseEntity<String> likePost(@PathVariable int id) {
        try {
            User user = userService.getAuthenticatedUser();
            Post post = postService.getById(id);
            String message = likeService.save(post, user, true)
                    ? "Post LIKED successfully!" : "LIKE removed successfully!";
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/posts/{id}/dislike")
    public ResponseEntity<String> dislikePost(@PathVariable int id) {
        try {
            User user = userService.getAuthenticatedUser();
            Post post = postService.getById(id);
            String message = likeService.save(post, user, false)
                    ? "Post DISLIKED successfully!" : "DISLIKE removed successfully!";
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/posts")
    public PostOutDto create(@Valid @RequestBody PostInDto postDto) {
        try {
            User user = userService.getAuthenticatedUser();
            Post postToCreate = postMapper.createPostFromDto(postDto, user);
            postService.create(postToCreate, user);
            return postMapper.postToPostOutDto(postToCreate);
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }

    @PutMapping("/posts/{id}")
    public PostOutDto update(@PathVariable int id, @Valid @RequestBody UpdatePostDto postDto) {
        try {
            User user = userService.getAuthenticatedUser();
            Post postToUpdate = postMapper.UpdatePostFromDto(postDto, id);
            postService.update(postToUpdate, user);
            Post updatedPost = postService.getById(id);
            return postMapper.postToPostOutDto(updatedPost);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }

    @DeleteMapping("/posts/{id}")
    public void delete(@PathVariable int id) {
        try {
            User user = userService.getAuthenticatedUser();
            postService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }
}
