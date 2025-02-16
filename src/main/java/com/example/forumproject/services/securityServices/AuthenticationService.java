package com.example.forumproject.services.securityServices;

import com.example.forumproject.models.dtos.userDtos.LoginDto;
import com.example.forumproject.models.dtos.userDtos.UserRegistrationDto;

public interface AuthenticationService {

    String authenticate(LoginDto request);

    String register(UserRegistrationDto request);

    void registerForMvc(UserRegistrationDto request);
}