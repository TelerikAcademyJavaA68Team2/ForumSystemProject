package com.example.forumproject.services.securityServices;

import com.example.forumproject.models.dtos.homepageResponseDtos.LoginDto;
import com.example.forumproject.models.dtos.homepageResponseDtos.UserRegistrationDto;

public interface AuthenticationService {

    public String register(UserRegistrationDto request);

    public String authenticate(LoginDto request);
}
