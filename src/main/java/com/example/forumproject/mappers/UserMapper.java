package com.example.forumproject.mappers;

import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public User registrationFromDto(UserDto userDto) {

        User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getUsername(), userDto.getPassword());
        user.setBlocked(false);
        user.setAdmin(false);
        return user;
    }


}
