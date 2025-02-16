package com.example.forumproject.controllers.rest;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.InvalidEmailFormatException;
import com.example.forumproject.exceptions.InvalidUserInputException;
import com.example.forumproject.mappers.HomepageResponseFactory;
import com.example.forumproject.models.dtos.homepageResponseDtos.HomepagePostsDto;
import com.example.forumproject.models.dtos.homepageResponseDtos.LoginDto;
import com.example.forumproject.models.dtos.homepageResponseDtos.UserRegistrationDto;
import com.example.forumproject.services.securityServices.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/home")
@Tag(name = "Homepage Management", description = "APIs for managing homepage")
public class HomepageController {

    private final HomepageResponseFactory homepageResponseFactory;

    @Autowired
    public HomepageController(HomepageResponseFactory homepageResponseFactory) {
        this.homepageResponseFactory = homepageResponseFactory;
    }

    @Operation(summary = "Get Homepage Info", description = "Retrieve general information for the homepage", responses = {
            @ApiResponse(description = "Success", responseCode = "200")
    })
    @GetMapping
    public ResponseEntity<String> getHomepage() {
        return ResponseEntity.ok(homepageResponseFactory.getHomepageInfo());
    }

    @Operation(summary = "Get Homepage Posts", description = "Retrieve posts displayed on the homepage", responses = {
            @ApiResponse(description = "Success", responseCode = "200")
    })
    @GetMapping("/posts")
    public ResponseEntity<HomepagePostsDto> getHomepagePosts(
            @RequestParam(required = false) String orderBy) {
        Optional<String> postsOrder = Optional.ofNullable(orderBy);
        return ResponseEntity.ok(homepageResponseFactory.getHomepagePosts(postsOrder.orElse("default")));
    }

}