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
@Tag(name = "Homepage Management", description = "APIs for managing homepage, authentication, and registration")
public class HomepageController {

    private final AuthenticationService authService;
    private final HomepageResponseFactory homepageResponseFactory;

    @Autowired
    public HomepageController(AuthenticationService authService, HomepageResponseFactory homepageResponseFactory) {
        this.authService = authService;
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

    @Operation(summary = "Register a New User", description = "Register a new user account", responses = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Conflict - Duplicate user", responseCode = "409"),
            @ApiResponse(description = "Bad Request - Invalid email format", responseCode = "400")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegistrationDto request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (InvalidEmailFormatException | InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "User Login", description = "Authenticate an existing user", responses = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Bad Request - User not found", responseCode = "400"),
            @ApiResponse(description = "Unauthorized - Account blocked", responseCode = "401")
    })
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

    @Operation(
            summary = "Get Login Information",
            description = "Retrieve information related to the login process, such as tips or instructions.",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200")
            }
    )
    @GetMapping("/login")
    public ResponseEntity<String> getLoginInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getLoginInfo());
    }

    @Operation(summary = "Get Register Info", description = "Retrieve registration-related information", responses = {
            @ApiResponse(description = "Success", responseCode = "200")
    })
    @GetMapping("/register")
    public ResponseEntity<String> getRegisterInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getRegisterInfo());
    }
}