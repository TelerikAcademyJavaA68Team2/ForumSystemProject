package com.example.forumproject.mappers;

import com.example.forumproject.models.dtos.homepageResponseDtos.HomepagePostsDto;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HomepageResponseFactory {

    private final UserService userService;
    private final PostService postService;
    private final PostMapper postMapper;

    @Autowired
    public HomepageResponseFactory(UserService userService, PostService postService, PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.userService = userService;
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

        sb.append("Welcome to the best Car Forum EVER! :").append(System.lineSeparator());
        sb.append(String.format("Active users: %d", userService.getNumberOfRegisteredUsers())).append(System.lineSeparator());
        sb.append(String.format("Posts created: %d", postService.getTotalNumberOfPosts())).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Possible Endpoints without registration:").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("/home/posts List the top 10 most commented posts.").append(System.lineSeparator());
        sb.append("/home/posts/recent List the top 10 most recent posts.").append(System.lineSeparator());
        sb.append("/home/register Register a new user.").append(System.lineSeparator());
        sb.append("/home/login Log in a user.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Possible Endpoints after registration:").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("/posts: Create and view posts.").append(System.lineSeparator());
        sb.append("/posts/{postId}: View, edit, or delete a specific post.").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("/users/profile: Get and update the user’s profile.").append(System.lineSeparator());
        sb.append("/users/posts: Create, edit, view, and delete user's posts.").append(System.lineSeparator());
        sb.append("/users/posts/{postId}/comments: Add comments to posts.").append(System.lineSeparator());
        sb.append("/users/posts/{postId}/like: Like or dislike a post.").append(System.lineSeparator());

        return sb.toString();
    }

    public HomepagePostsDto getHomepagePosts(String orderBy) {
        if (orderBy.equalsIgnoreCase("recent")) {
            HomepagePostsDto homepagePosts = new HomepagePostsDto("These are the Forums newest posts!");
            PostFilterOptions filterOptions = new PostFilterOptions(null, null, null, null, null, "id", "desc", null);
            homepagePosts.setPosts(postService.getAll(filterOptions).stream().limit(10).map(postMapper::postToPostOutDto).toList());
            return homepagePosts;
        } else {
            HomepagePostsDto homepagePosts = new HomepagePostsDto("These are the Forums top commented posts!");
            PostFilterOptions filterOptions = new PostFilterOptions(null, null, null, null, null, "comments", "desc", null);
            homepagePosts.setPosts(postService.getAll(filterOptions).stream().limit(10).map(postMapper::postToPostOutDto).toList());
            return homepagePosts;
        }
    }
}