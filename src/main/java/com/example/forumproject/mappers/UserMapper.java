package com.example.forumproject.mappers;

import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.adminResponceDtos.FullProfileAdminDto;
import com.example.forumproject.models.dtos.adminResponceDtos.FullProfileUserDto;
import com.example.forumproject.models.dtos.userDtos.UserResponseDto;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PostService postService;
    private final PostMapper postMapper;

    @Autowired
    public UserMapper(PostService postService, PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }


    public UserResponseDto mapUserToDtoOut(User user) {
        UserResponseDto userOut = new UserResponseDto(user.getId(),
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
            PostFilterOptions filterOptions = new PostFilterOptions
                    (null, null, null,
                            null, null,
                            null, null, user.getUsername());
            userDto.setUser_posts(postService.getAll(filterOptions).stream().map(postMapper::postToPostOutDto).toList());
            userDto.setUser_posts(postService.getAll(filterOptions).stream().map(postMapper::postToPostOutDto).toList());
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

            PostFilterOptions filterOptions = new PostFilterOptions
                    (null, null, null,
                            null, null,
                            null, null, user.getUsername());
            userDto.setUser_posts(postService.getAll(filterOptions).stream().map(postMapper::postToPostOutDto).toList());
            return userDto;
        }
    }
}
