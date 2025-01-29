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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final AuthenticationService authService;

    @Autowired
    public ProfileController(UserMapper userMapper, UserService userService, AuthenticationService authService) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<Object> getProfile() {
        User user = userService.getAuthenticatedUser();
        return ResponseEntity.ok(userMapper.mapUserToUserFullProfileOutDto(user));
    }

    @DeleteMapping
    public ResponseEntity<String> getProfile(@Valid @RequestBody LoginDto request) {
        authService.authenticate(request);
        userService.deleteUser();
        return ResponseEntity.ok("Account deleted successfully");
    }

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
