package com.example.forumproject.services;

import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "9bfb230cbadfdf8af34f02be46188a222b105d14a1dd1f183e8934de0d78c354";

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails user) {
        try {
            String username = extractUsername(token);
            if (!username.equals(user.getUsername())) {
                throw new UnauthorizedAccessException("Please log in again.");
            }
            return !isTokenExpired(token);
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Invalid token: " + e.getMessage());
        }
    }

    private boolean isTokenExpired(String token) {

        if (extractExpiration(token).before(new Date())) {
            throw new UnauthorizedAccessException("JWT Token has expired. Please log in again.");
        }
        return false;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSigninKey())
                .compact();
        return token;
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
