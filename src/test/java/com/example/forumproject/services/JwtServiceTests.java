package com.example.forumproject.services;

import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.models.User;
import com.example.forumproject.services.securityServices.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTests {

    @Mock
    private UserDetails mockUserDetails;

    @InjectMocks
    JwtServiceImpl service;

    @Test
    void generateToken_ShouldGenerateValidToken_Always() {
        // Arrange
        User mockUser = new User("TestUser", "password");

        // Act
        String token = service.generateToken(mockUser);

        // Assert
        Assertions.assertNotNull(token);
        Assertions.assertTrue(token.startsWith("eyJhbGciOiJIUzM4NCJ9"));
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername_FromToken() {
        // Arrange
        String mockUsername = "testUser";
        String token = service.generateToken(new User(mockUsername, "password"));

        // Act
        String extractedUsername = service.extractUsername(token);

        // Assert
        Assertions.assertEquals(mockUsername, extractedUsername);
    }

    @Test
    void isValid_ShouldReturnTrue_When_TokenIsValid() {
        // Arrange
        String mockUsername = "testUser";
        String token = service.generateToken(new User(mockUsername, "password"));

        Mockito.when(mockUserDetails.getUsername()).thenReturn(mockUsername);

        // Act
        boolean isValid = service.isValid(token, mockUserDetails);

        // Assert
        Assertions.assertTrue(isValid);
    }

    @Test
    void isValid_ShouldThrowUnauthorizedAccessException_When_TokenIsInvalid() {
        // Arrange
        String invalidUsername = "invalidUser";
        String token = service.generateToken(new User("testUser", "password"));

        Mockito.when(mockUserDetails.getUsername()).thenReturn(invalidUsername);

        // Act & Assert
        UnauthorizedAccessException exception = Assertions.assertThrows(UnauthorizedAccessException.class, () -> {
            service.isValid(token, mockUserDetails);
        });

        Assertions.assertEquals("Invalid token: Please log in again.", exception.getMessage());
    }

    @Test
    void extractClaim_ShouldReturnCorrectClaim_WhenTokenIsValid() {
        // Arrange
        String mockUsername = "testuser";
        String token = service.generateToken(new User(mockUsername, "password"));

        // Act
        String extractedSubject = service.extractClaim(token, Claims::getSubject);

        // Assert
        Assertions.assertEquals(mockUsername, extractedSubject);
    }
}