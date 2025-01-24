package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.mappers.PostMapper;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.CreatePostDto;
import com.example.forumproject.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    public static final String UNAUTHORIZED_CREATE_POST_MESSAGE =
            "Invalid username or password, only existing users can create new beers!";
    public static final String POST_ID_NOT_FOUND = "Post with id %d not found.";

    private final PostService postService;

    private final PostMapper postMapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public PostController(PostService postService, PostMapper postMapper, AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.authenticationHelper = authenticationHelper;
    }


    @GetMapping("/{id}")
    public Post getById(@PathVariable int id) {
        try {
            return postService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException
                    (HttpStatus.NOT_FOUND, String.format(POST_ID_NOT_FOUND, id));
        }
    }

    @PostMapping
    public Post create(@RequestHeader HttpHeaders headers, @Valid @RequestBody CreatePostDto postDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post postToCreate = postMapper.createPostFromDto(postDto, user);
            postService.create(postToCreate, user);
            return postToCreate;
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    UNAUTHORIZED_CREATE_POST_MESSAGE);
        }

    }
}
