package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.InvalidEmailFormatException;
import com.example.forumproject.mappers.HomepageResponseFactory;
import com.example.forumproject.models.dtos.HomepagePostsDto;
import com.example.forumproject.models.dtos.LoginDto;
import com.example.forumproject.models.dtos.UserInDto;
import com.example.forumproject.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/home")
public class HomepageController {

    private final AuthenticationService authService;
    private final HomepageResponseFactory homepageResponseFactory;

    @Autowired
    public HomepageController(AuthenticationService authService, HomepageResponseFactory homepageResponseFactory) {
        this.authService = authService;
        this.homepageResponseFactory = homepageResponseFactory;
    }

    @GetMapping
    public ResponseEntity<String> getHomepage() {
        return ResponseEntity.ok(homepageResponseFactory.getHomepageInfo());
    }

    //todo /posts
    @GetMapping("/posts")
    public ResponseEntity<HomepagePostsDto> getHomepagePosts() {
        return ResponseEntity.ok(homepageResponseFactory.getHomepagePosts());
    }

    @GetMapping("/posts/recent")
    public ResponseEntity<HomepagePostsDto> getHomepageRecentPosts() {
        return ResponseEntity.ok(homepageResponseFactory.getHomepageRecentPosts());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserInDto request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (InvalidEmailFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto request) {
        try {
            return ResponseEntity.ok(authService.authenticate(request));
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your account is blocked!");
        }
    }

    @GetMapping("/login")
    public ResponseEntity<String> getLoginInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getLoginInfo());
    }

    @GetMapping("/register")
    public ResponseEntity<String> getRegisterInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getRegisterInfo());
    }
}
