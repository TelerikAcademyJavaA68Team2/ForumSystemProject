package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.mappers.PostMapper;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.CreatePostDto;
import com.example.forumproject.models.dtos.UpdatePostDto;
import com.example.forumproject.services.contracts.LikeService;
import com.example.forumproject.services.contracts.PostService;
import com.example.forumproject.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
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

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAll();
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable int id) {
        try {
            return postService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException
                    (HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/like")
    public String likePost(@PathVariable int id) {
        try {
            User user = userService.getAuthenticatedUser();
            Post post = postService.getById(id);
            return likeService.save(post, user, true) ? "Post LIKED successfully!" : "LIKE removed successfully!";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}/dislike")
    public String dislikePost(@PathVariable int id) {
        try {
            User user = userService.getAuthenticatedUser();
            Post post = postService.getById(id);
            return likeService.save(post, user, false) ? "Post DISLIKED successfully!" : "DISLIKE removed successfully!";


        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public Post create(@Valid @RequestBody CreatePostDto postDto) {
        try {
            User user = userService.getAuthenticatedUser();
            Post postToCreate = postMapper.createPostFromDto(postDto, user);
            postService.create(postToCreate, user);
            return postToCreate;
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public Post update(@PathVariable int id, @Valid @RequestBody UpdatePostDto postDto) {
        try {
            User user = userService.getAuthenticatedUser();
            Post postToUpdate = postMapper.UpdatePostFromDto(postDto, id);
            postService.update(postToUpdate, user);
            return postToUpdate;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
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
