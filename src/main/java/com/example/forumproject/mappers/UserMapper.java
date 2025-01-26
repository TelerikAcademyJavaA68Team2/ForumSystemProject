package com.example.forumproject.mappers;

import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.UserInDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public User registrationFromDto(UserInDto userInDto) {

        User user = new User(userInDto.getFirstName(), userInDto.getLastName(), userInDto.getEmail(), userInDto.getUsername(), userInDto.getPassword());
        user.setBlocked(false);
        user.setAdmin(false);
        return user;
    }


}
