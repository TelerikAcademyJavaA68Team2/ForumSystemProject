package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.InvalidUserInputException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.mappers.HomepageResponseFactory;
import com.example.forumproject.mappers.UserMapper;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.userDtos.UserResponseDto;
import com.example.forumproject.models.filterOptions.UsersFilterOptions;
import com.example.forumproject.services.commentService.CommentService;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management", description = "Endpoints for admin actions like user, post, and comment management")
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

    @Operation(
            summary = "Retrieve all users",
            description = "Fetch a list of users with optional filters and sorting options",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200")
            }
    )
    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers(@RequestParam(required = false) String first_name,
                                             @RequestParam(required = false) String username,
                                             @RequestParam(required = false) String email,
                                             @RequestParam(required = false) Long minPosts,
                                             @RequestParam(required = false) Long maxPosts,
                                             @RequestParam(required = false) String account_type,
                                             @RequestParam(required = false) String account_status,
                                             @RequestParam(required = false) String orderBy,
                                             @RequestParam(required = false) String orderType) {
        UsersFilterOptions filterOptions = new UsersFilterOptions(first_name, username, email,
                minPosts, maxPosts, account_type, account_status, orderBy, orderType);
        List<User> users = userService.getAllUsers(filterOptions);
        return users.stream().map(userMapper::mapUserToDtoOut).toList();
    }

    @Operation(
            summary = "Retrieve user by ID",
            description = "Fetch details of a user using their ID",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "User not found", responseCode = "404")
            }
    )
    @GetMapping("/users/{userId}")
    public Object getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getById(userId);
            return userMapper.mapUserToUserFullProfileOutDto(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(
            summary = "Update phone number",
            description = "Update the phone number of the authenticated user",
            responses = {
                    @ApiResponse(description = "Phone updated successfully", responseCode = "201"),
                    @ApiResponse(description = "Invalid phone number", responseCode = "400")
            }
    )
    @PostMapping("/profile/phone") // reed todo below combine with /profile post
    public ResponseEntity<String> updatePhoneNumber(@RequestBody String number) {
        try {
            userService.updatePhoneNumber(userService.getAuthenticatedUser(), number);
            return ResponseEntity.status(HttpStatus.CREATED).body("Phone updated successfully!");
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


   /* @GetMapping("/profile")  //todo could do the phoneNumber update in postMapping /profile
    public ResponseEntity<String> getAdminProfile() {
        try {

        }
    }*/

    @Operation(
            summary = "Delete a post",
            description = "Remove a specific post by ID",
            responses = {
                    @ApiResponse(description = "Post deleted successfully", responseCode = "201"),
                    @ApiResponse(description = "Post not found", responseCode = "404"),
                    @ApiResponse(description = "Invalid user input", responseCode = "400")
            }
    )
    @PostMapping("/posts/{postId}/delete")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        try {
            postService.delete(postId, userService.getAuthenticatedUser());
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Post with id: %d was deleted successfully!", postId));
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            summary = "Delete a comment",
            description = "Remove a specific comment from a post by ID",
            responses = {
                    @ApiResponse(description = "Comment deleted successfully", responseCode = "201"),
                    @ApiResponse(description = "Comment not found", responseCode = "404"),
                    @ApiResponse(description = "Invalid user input", responseCode = "400")
            }
    )
    @PostMapping("/posts/{postId}/comments/{commentId}/delete")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        try {
            commentService.delete(postId, commentId);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format
                    ("Comment with id: %d was deleted from Post with id: %d successfully!", commentId, postId));
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            summary = "Block a user",
            description = "Block a specific user by their ID",
            responses = {
                    @ApiResponse(description = "User blocked successfully", responseCode = "201"),
                    @ApiResponse(description = "User not found", responseCode = "404"),
                    @ApiResponse(description = "Invalid user input", responseCode = "400")
            }
    )
    @PostMapping("/users/{userId}/block")
    public ResponseEntity<String> blockUser(@PathVariable Long userId) {
        try {
            userService.blockUser(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("User with id: %d was blocked successfully!", userId));
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            summary = "Unblock a user",
            description = "Unblock a specific user by their ID",
            responses = {
                    @ApiResponse(description = "User unblocked successfully", responseCode = "201"),
                    @ApiResponse(description = "User not found", responseCode = "404"),
                    @ApiResponse(description = "Invalid user input", responseCode = "400")
            }
    )
    @PostMapping("/users/{userId}/unblock")
    public ResponseEntity<String> unblockUser(@PathVariable Long userId) {
        try {
            userService.unblockUser(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("User with id: %d was unblocked successfully!", userId));
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            summary = "Promote a user to Admin",
            description = "Grant admin privileges to a user by their ID",
            responses = {
                    @ApiResponse(description = "User promoted to admin successfully", responseCode = "201"),
                    @ApiResponse(description = "User not found", responseCode = "404"),
                    @ApiResponse(description = "Invalid user input", responseCode = "400")
            }
    )
    @PostMapping("/users/{userId}/make-admin")
    public ResponseEntity<String> updateToAdmin(@PathVariable Long userId) {
        try {
            userService.promoteToAdmin(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("User with id: %d was promoted to Admin successfully!", userId));
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            summary = "Demote an Admin to User",
            description = "Revoke admin privileges and make the user a regular user",
            responses = {
                    @ApiResponse(description = "Admin demoted to user successfully", responseCode = "201"),
                    @ApiResponse(description = "User not found", responseCode = "404"),
                    @ApiResponse(description = "Invalid user input", responseCode = "400")
            }
    )
    @PostMapping("/users/{userId}/make-user")
    public ResponseEntity<String> demoteToUser(@PathVariable Long userId) {
        try {
            userService.demoteAdminToUser(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Admin with id: %d was demoted to User successfully!", userId));
        } catch (InvalidUserInputException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    // informational delete on production
    // informational delete on production
    // informational delete on production
    // informational delete on production
    // informational delete on production
    // informational delete on production
    @Operation(
            summary = "Get Admin Options Info",
            description = "Retrieve general information about admin options available in the application.",
            responses = @ApiResponse(description = "Success", responseCode = "200")
    )
    @GetMapping
    public ResponseEntity<String> getAdminOptionsInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getAdminOptionsInfo());
    }

    @Operation(
            summary = "Get Delete Comment Info",
            description = "Retrieve information about the process of deleting a specific comment from a post.",
            responses = @ApiResponse(description = "Success", responseCode = "200")
    )
    @GetMapping("/posts/{postId}/comments/{commentId}/delete")
    public ResponseEntity<String> getDeleteCommentInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getDeleteCommentInfo());
    }

    @Operation(
            summary = "Get Delete Post Info",
            description = "Retrieve information about the process of deleting a specific post.",
            responses = @ApiResponse(description = "Success", responseCode = "200")
    )
    @GetMapping("/posts/{postId}/delete")
    public ResponseEntity<String> getDeletePostInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getDeletePostInfo());
    }

    @Operation(
            summary = "Get Promote to Admin Info",
            description = "Retrieve information about the process of promoting a user to an admin role.",
            responses = @ApiResponse(description = "Success", responseCode = "200")
    )
    @GetMapping("/users/{userId}/make-admin")
    public ResponseEntity<String> getUpdateToAdminInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getUpdateToAdminInfo());
    }

    @Operation(
            summary = "Get Demote to User Info",
            description = "Retrieve information about the process of demoting an admin to a regular user role.",
            responses = @ApiResponse(description = "Success", responseCode = "200")
    )
    @GetMapping("/users/{userId}/make-user")
    public ResponseEntity<String> getDemoteToUserInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getDemoteToUserInfo());
    }

    @Operation(
            summary = "Get Unblock User Info",
            description = "Retrieve information about the process of unblocking a user account.",
            responses = @ApiResponse(description = "Success", responseCode = "200")
    )
    @GetMapping("/users/{userId}/unblock")
    public ResponseEntity<String> getUnblockUserInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getUnblockUserInfo());
    }

    @Operation(
            summary = "Get Block User Info",
            description = "Retrieve information about the process of blocking a user account.",
            responses = @ApiResponse(description = "Success", responseCode = "200")
    )
    @GetMapping("/users/{userId}/block")
    public ResponseEntity<String> getBlockUserInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getBlockUserInfo());
    }

    @Operation(
            summary = "Get Update Phone Info",
            description = "Retrieve information about the process of updating a user's phone number.",
            responses = @ApiResponse(description = "Success", responseCode = "200")
    )
    @GetMapping("/profile/phone")
    public ResponseEntity<String> getUpdatePhoneInfo() {
        return ResponseEntity.ok(homepageResponseFactory.getUpdatePhoneInfo());
    }
}