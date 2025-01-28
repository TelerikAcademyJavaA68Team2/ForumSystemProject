package com.example.forumproject.services.securityServices;

import com.example.forumproject.models.dtos.homepageResponseDtos.LoginDto;
import com.example.forumproject.models.dtos.homepageResponseDtos.UserRegistrationDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    String authenticate(LoginDto request);

    String register(UserRegistrationDto request);
}