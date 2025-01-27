package com.example.forumproject.mappers;

import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.FullProfileAdminDto;
import com.example.forumproject.models.dtos.FullProfileUserDto;
import com.example.forumproject.models.dtos.UserOutDto;
import com.example.forumproject.services.contracts.PostService;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PostService postService;
    private final PostMapper postMapper;


    public UserMapper(PostService postService, PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }


    public UserOutDto mapUserToDtoOut(User user) {
        UserOutDto userOut = new UserOutDto(user.getId(),
                user.getFirstName(),
                user.getUsername(),
                user.getEmail(),
                user.isAdmin() ? "ADMIN" : "USER",
                user.isBlocked() ? "BLOCKED ACCOUNT" : "ACTIVE ACCOUNT");
        userOut.setNumberOfPosts(postService.getNumberOfPostsByUser(user.getId()));
        return userOut;
    }

    public Object mapUserToUserFullProfileOutDto(User user) {
        String accountType = user.isAdmin() ? "ADMIN" : "USER";
        String accountStatus = user.isBlocked() ? "BLOCKED ACCOUNT" : "ACTIVE ACCOUNT";
        String profilePhoto = user.getPhoto() == null ? "No profile photo" : user.getPhoto();

        if (user.isAdmin()) {
            FullProfileAdminDto userDto = new FullProfileAdminDto(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername(),
                    user.getEmail(),
                    accountType,
                    accountStatus,
                    profilePhoto,
                    user.getPhoneNumber() == null ? "No phone number provided" : user.getPhoneNumber());
            userDto.setUser_posts(postService.getAllPostsFromUser(user.getId()).stream().map(postMapper::postToPostOutDto).toList());
            return userDto;
        } else {
            FullProfileUserDto userDto = new FullProfileUserDto(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername(),
                    user.getEmail(),
                    accountType,
                    accountStatus,
                    profilePhoto);
            userDto.setUser_posts(postService.getAllPostsFromUser(user.getId()).stream().map(postMapper::postToPostOutDto).toList());
            return userDto;
        }
    }
}
