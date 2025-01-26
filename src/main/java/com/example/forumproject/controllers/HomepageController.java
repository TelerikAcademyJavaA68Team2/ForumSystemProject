package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.InvalidEmailFormatException;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.HomepageResponseFactory;
import com.example.forumproject.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (InvalidEmailFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
