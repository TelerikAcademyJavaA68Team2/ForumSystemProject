package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.InvalidUserInputException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.helpers.ValidationHelpers;
import com.example.forumproject.mappers.UserMapper;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.homepageResponseDtos.LoginDto;
import com.example.forumproject.models.dtos.userDtos.RequestUserProfileDto;
import com.example.forumproject.services.UserService;
import com.example.forumproject.services.securityServices.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "Profile Management", description = "API for managing user profiles")
public class ProfileController {

    public static final String ACCOUNT_DELETED_SUCCESSFULLY = "Account deleted successfully";

    private final UserMapper userMapper;
    private final UserService userService;
    private final AuthenticationService authService;

    @Autowired
    public ProfileController(UserMapper userMapper, UserService userService, AuthenticationService authService) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.authService = authService;
    }

    @Operation(
            summary = "Get user profile",
            description = "Retrieve the profile information of the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
                    @ApiResponse(responseCode = "401", description = "User not authenticated")
            }
    )
    @GetMapping
    public ResponseEntity<Object> getProfile() {
        User user = userService.getAuthenticatedUser();
        return ResponseEntity.ok(userMapper.mapUserToUserFullProfileOutDto(user));
    }

    @Operation(
            summary = "Delete user account",
            description = "Delete the account of the currently authenticated user after confirming login credentials.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account deleted successfully"),
                    @ApiResponse(responseCode = "401", description = "User not authenticated"),
            }
    )
    @DeleteMapping
    public ResponseEntity<String> getProfile(@Valid @RequestBody LoginDto request) {
        authService.authenticate(request);
        userService.deleteUser();
        return ResponseEntity.ok(ACCOUNT_DELETED_SUCCESSFULLY);
    }

    @Operation(
            summary = "Update user profile",
            description = "Update the profile information of the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
                    @ApiResponse(responseCode = "409", description = "Duplicate entity conflict"),
                    @ApiResponse(responseCode = "403", description = "User not authorized to update profile"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PatchMapping
    public ResponseEntity<String> updateProfile(@Valid @RequestBody RequestUserProfileDto userUpdateDto) {
        try {
            User user = userService.getAuthenticatedUser();
            String result = ValidationHelpers.ValidateUpdate(userUpdateDto, user);
            userService.update(user);
            return ResponseEntity.ok(result);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
