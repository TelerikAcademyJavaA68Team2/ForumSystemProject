package com.example.forumproject.services.securityServices;

import com.example.forumproject.models.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {

    public String extractUsername(String token);

    boolean isValid(String token, UserDetails user);

    <T> T extractClaim(String token, Function<Claims, T> resolver);

    String generateToken(User user);
}
