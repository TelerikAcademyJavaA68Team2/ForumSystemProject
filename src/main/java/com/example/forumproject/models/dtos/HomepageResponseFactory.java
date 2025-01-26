package com.example.forumproject.models.dtos;

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


    public String getHomepageInfo() {
        return String.format("Active users: %d%nPosts created: %d%n%nPossible Endpoints:\n" +
                " \n" +
                " \n" +
                "/posts: Create and view posts.\n" +
                "/posts/{postId}: View, edit, or delete a specific post.\n" +
                " \n" +
                " \n" +
                "/auth/login: Login a user and return a token.\n" +
                "/auth/logout: Log out the user.\n" +
                "/auth/refresh: Refresh authentication token.\n" +
                " \n" +
                " \n" +
                "/admin/users: Search for users and manage their status (block, unblock).\n" +
                "/admin/users/{userId}/make-admin: Make a user an admin.\n" +
                "/admin/posts: List and delete any post.\n" +
                " \n" +
                " \n" +
                "/users/profile: Get and update the user’s profile.\n" +
                "/users/posts: Create, edit, view, and delete user's posts.\n" +
                "/users/posts/{postId}/comments: Add comments to posts.\n" +
                "/users/posts/{postId}/like: Like or dislike a post.\n" +
                " \n" +
                " \n" +
                "/public/home: Get the forum's public data (e.g., number of users, posts).\n" +
                "/public/posts: List the top 10 most commented or most recent posts.\n" +
                "/public/register: Register a new user.\n" +
                "/public/login: Log in a user.", userRepository.getAllUsers().stream().count(), postRepository.getAll().stream().count());
    }


}
