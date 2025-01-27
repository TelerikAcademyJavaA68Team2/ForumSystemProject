package com.example.forumproject.controllers;


import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.InvalidUserInputException;
import com.example.forumproject.mappers.HomepageResponseFactory;
import com.example.forumproject.mappers.UserMapper;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.UserOutDto;
import com.example.forumproject.services.contracts.CommentService;
import com.example.forumproject.services.contracts.PostService;
import com.example.forumproject.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final HomepageResponseFactory homepageResponseFactory;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final UserMapper userMapper;

    @Autowired
    public AdminController(
            HomepageResponseFactory homepageResponseFactory,
            UserMapper userMapper,
            UserService userService,
            PostService postService,
            CommentService commentService) {
        this.homepageResponseFactory = homepageResponseFactory;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<String> getAdminOptionsInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getAdminOptionsInfo());
    }


    @PostMapping("/profile/phone")
    public ResponseEntity<String> updatePhoneNumber(@RequestBody String number) {
        try {
            userService.updatePhoneNumber(userService.getAuthenticatedUser(), number);
            return ResponseEntity.status(HttpStatus.CREATED).body("Phone updated successfully!");
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

  /*  @GetMapping("/profile")
    public ResponseEntity<String> getAdminProfile() {
        try {
            userService.updatePhoneNumber(userService.getAuthenticatedUser(), number);
            return ResponseEntity.status(HttpStatus.CREATED).body("Phone updated successfully!");
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }*/

    @PostMapping("/users/{userId}/block")
    public ResponseEntity<String> blockUser(@PathVariable int userId) {
        try {
            userService.blockUser(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("User with id: %d was blocked successfully!", userId));
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/users/{userId}/unblock")
    public ResponseEntity<String> unblockUser(@PathVariable int userId) {
        try {
            userService.unblockUser(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("User with id: %d was unblocked successfully!", userId));
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/users/{userId}/make-admin")
    public ResponseEntity<String> updateToAdmin(@PathVariable int userId) {
        try {
            userService.promoteToAdmin(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("User with id: %d was promoted to Admin successfully!", userId));
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/users/{userId}/make-user")
    public ResponseEntity<String> demoteToUser(@PathVariable int userId) {
        try {
            userService.demoteAdminToUser(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Admin with id: %d was demoted to User successfully!", userId));
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/posts/{postId}/delete")
    public ResponseEntity<String> deletePost(@PathVariable int postId) {
        try {
            postService.delete(postId, userService.getAuthenticatedUser());
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Post with id: %d was deleted successfully!", postId));
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/delete")
    public ResponseEntity<String> deleteComment(@PathVariable int postId, @PathVariable int commentId) {
        try {
            commentService.delete(postId, commentId, userService.getAuthenticatedUser());
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format
                    ("Comment with id: %d was deleted from Post with id: %d successfully!", commentId, postId));
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/users/{id}")
    public Object getUserById(@PathVariable int id) {
        try {
            User user = userService.getById(id);
            return userMapper.mapUserToUserFullProfileOutDto(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/posts/{postId}/comments/{commentId}/delete")
    public ResponseEntity<String> getDeleteCommentInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getDeleteCommentInfo());
    }

    @GetMapping("/posts/{postId}/delete")
    public ResponseEntity<String> getDeletePostInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getDeletePostInfo());
    }

    @GetMapping("/users/{userId}/make-admin")
    public ResponseEntity<String> getUpdateToAdminInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getUpdateToAdminInfo());
    }

    @GetMapping("/users/{userId}/make-user")
    public ResponseEntity<String> getDemoteToUserInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getDemoteToUserInfo());
    }

    @GetMapping("/users/{userId}/unblock")
    public ResponseEntity<String> getUnblockUserInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getUnblockUserInfo());
    }

    @GetMapping("/users/{userId}/block")
    public ResponseEntity<String> getBlockUserInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getBlockUserInfo());
    }

    @GetMapping("/profile/phone")
    public ResponseEntity<String> getUpdatePhoneInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getUpdatePhoneInfo());
    }

    @GetMapping("/users") // only admins can check users
    public List<UserOutDto> getAllUsers() {

        List<User> users = userService.getAllUsers();
        return users.stream().map(userMapper::mapUserToDtoOut).toList();
    }
}
