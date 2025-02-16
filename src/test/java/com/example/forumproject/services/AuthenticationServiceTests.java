package com.example.forumproject.services;

import com.example.forumproject.Helpers;
import com.example.forumproject.exceptions.InvalidUserInputException;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.userDtos.LoginDto;
import com.example.forumproject.models.dtos.userDtos.UserRegistrationDto;
import com.example.forumproject.services.securityServices.AuthenticationServiceImpl;
import com.example.forumproject.services.securityServices.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void register_ShouldReturnToken_WhenUserIsRegistered() {
        // Arrange
        UserRegistrationDto registrationRequest = Helpers.createMockAUserRegistrationDto();
        User user = Helpers.createMockUser();
        user.setId(null);
        Mockito.when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("encodedPassword123");
        Mockito.when(jwtService.generateToken(user)).thenReturn("generatedToken");

        // Act
        String token = authenticationService.register(registrationRequest);

        // Assert
        Assertions.assertEquals("generatedToken", token);
    }

    @Test
    void register_ShouldThrowExc_WhenPasswordConfirmInvalid() {
        // Arrange
        UserRegistrationDto registrationRequest = Helpers.createMockAUserRegistrationDto();
        registrationRequest.setPasswordConfirm("invalid");
        User user = Helpers.createMockUser();
        user.setId(null);

        // Act, Assert
        Assertions.assertThrows(InvalidUserInputException.class, () -> authenticationService.register(registrationRequest));
    }

    @Test
    void authenticate_ShouldReturnToken_WhenCredentialsAreValid() {
        // Arrange
        User user = Helpers.createMockUser();
        LoginDto loginRequest = Helpers.createMockALoginDto();
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        Mockito.when(userService.loadUserByUsername(loginRequest.getUsername())).thenReturn(user);
        Mockito.when(jwtService.generateToken(user)).thenReturn("generatedToken");

        // Act
        String token = authenticationService.authenticate(loginRequest);

        // Assert
        Assertions.assertEquals("generatedToken", token);
    }

    @Test
    void authenticate_ShouldThrowException_When_CredentialsAreNotValid() {
        // Arrange
        LoginDto loginRequest = Helpers.createMockALoginDto();
        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenThrow(BadCredentialsException.class);

        // Act & Assert
        Assertions.assertThrows(ResponseStatusException.class, () ->
                authenticationService.authenticate(loginRequest));
    }
}