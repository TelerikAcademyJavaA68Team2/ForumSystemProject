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

        StringBuilder sb = new StringBuilder(System.lineSeparator());

        sb.append("    Welcome to the best Car Forum EVER!").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(String.format(" Active users: %d           ", userService.getNumberOfRegisteredUsers()));
        sb.append(String.format(" %d Posts created!", postService.getTotalNumberOfPosts())).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(" Possible Endpoints without registration:").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(" /home/posts        ->    List of our top 10 most commented/recent posts.").append(System.lineSeparator());
        sb.append(" /home/register     ->    Register to begin your exciting journey with us!").append(System.lineSeparator());
        sb.append(" /home/login        ->    After you register you can get your amazing jwt token from here!");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(" But the real journey begins after registration!").append(System.lineSeparator());
        sb.append(System.lineSeparator());


        return sb.toString();
    }

    public HomepagePostsDto getHomepagePosts(String orderBy) {
        if (orderBy.equalsIgnoreCase("recent")) {
            HomepagePostsDto homepagePosts = new HomepagePostsDto("These are the Forums newest posts!");
            PostFilterOptions filterOptions = new PostFilterOptions(null, null, null, null, null, "id", "desc", null);
            homepagePosts.setPosts(postService.getAllPosts(filterOptions).stream().limit(10).map(postMapper::postToPostOutDto).toList());
            return homepagePosts;
        } else {
            HomepagePostsDto homepagePosts = new HomepagePostsDto("These are the Forums top commented posts!");
            PostFilterOptions filterOptions = new PostFilterOptions(null, null, null, null, null, "comments", "desc", null);
            homepagePosts.setPosts(postService.getAllPosts(filterOptions).stream().limit(10).map(postMapper::postToPostOutDto).toList());
            return homepagePosts;
        }
    }
}