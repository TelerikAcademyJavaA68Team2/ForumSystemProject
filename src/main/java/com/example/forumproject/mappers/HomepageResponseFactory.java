package com.example.forumproject.mappers;

import com.example.forumproject.repositories.contracts.PostRepository;
import com.example.forumproject.repositories.contracts.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HomepageResponseFactory {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public HomepageResponseFactory(UserRepository userRepository, PostRepository postRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public String getRegisterInfo() {
        StringBuilder sb = new StringBuilder();

        sb.append("Registration information!").append(System.lineSeparator());
        sb.append("In order to register you need to fill this form (its example only).").append(System.lineSeparator());
        sb.append("And do a POST request to /home/register   (the current URL)").append(System.lineSeparator());
        sb.append("Fill this information in the \"raw\" option in your request body!").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("{").append(System.lineSeparator());
        sb.append("    \"firstName\": \"YOUR FIRST NAME\",").append(System.lineSeparator());
        sb.append("    \"lastName\": \"YOUR LAST NAME\",").append(System.lineSeparator());
        sb.append("    \"email\": \"Valid@email.com\",").append(System.lineSeparator());
        sb.append("    \"username\": \"your username\",").append(System.lineSeparator());
        sb.append("    \"password\": \"your password\"").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());

        return sb.toString();
    }

    public String getLoginInfo() {
        StringBuilder sb = new StringBuilder();

        sb.append("Login information!").append(System.lineSeparator());
        sb.append("In order to Login you need to fill this form (its example only).").append(System.lineSeparator());
        sb.append("And do a POST request to /home/login   (the current URL)").append(System.lineSeparator());
        sb.append("Fill this information in the \"raw\" option in your request body!").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("{").append(System.lineSeparator());
        sb.append("    \"username\": \"your username\",").append(System.lineSeparator());
        sb.append("    \"password\": \"your password\"").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());

        return sb.toString();
    }

    public String getHomepageInfo() {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Active users: %d%n", userRepository.getNumberOfActiveUsers()));
        sb.append(String.format("Posts created: %d%n%n", postRepository.getTotalNumberOfPosts()));

        sb.append("Possible Endpoints:").append(System.lineSeparator()).append(System.lineSeparator());

        sb.append("/posts: Create and view posts.").append(System.lineSeparator());
        sb.append("/posts/{postId}: View, edit, or delete a specific post.").append(System.lineSeparator()).append(System.lineSeparator());

        sb.append("/auth/login: Login a user and return a token.").append(System.lineSeparator());
        sb.append("/auth/logout: Log out the user.").append(System.lineSeparator());
        sb.append("/auth/refresh: Refresh authentication token.").append(System.lineSeparator()).append(System.lineSeparator());

        sb.append("/admin/users: Search for users and manage their status (block, unblock).").append(System.lineSeparator());
        sb.append("/admin/users/{userId}/make-admin: Make a user an admin.").append(System.lineSeparator());
        sb.append("/admin/posts: List and delete any post.").append(System.lineSeparator()).append(System.lineSeparator());

        sb.append("/users/profile: Get and update the user’s profile.").append(System.lineSeparator());
        sb.append("/users/posts: Create, edit, view, and delete user's posts.").append(System.lineSeparator());
        sb.append("/users/posts/{postId}/comments: Add comments to posts.").append(System.lineSeparator());
        sb.append("/users/posts/{postId}/like: Like or dislike a post.").append(System.lineSeparator()).append(System.lineSeparator());

        sb.append("/public/home: Get the forum's public data (e.g., number of users, posts).").append(System.lineSeparator());
        sb.append("/public/posts: List the top 10 most commented or most recent posts.").append(System.lineSeparator());
        sb.append("/public/register: Register a new user.").append(System.lineSeparator());
        sb.append("/public/login: Log in a user.").append(System.lineSeparator());

        return sb.toString();
    }


}
