package com.example.forumproject.helpers;

import com.example.forumproject.models.User;
import com.example.forumproject.models.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public User fromDto(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        return user;
    }
}
