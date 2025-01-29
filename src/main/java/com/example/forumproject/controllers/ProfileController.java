package com.example.forumproject.controllers;

import com.example.forumproject.mappers.UserMapper;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.homepageResponseDtos.LoginDto;
import com.example.forumproject.services.UserService;
import com.example.forumproject.services.securityServices.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
