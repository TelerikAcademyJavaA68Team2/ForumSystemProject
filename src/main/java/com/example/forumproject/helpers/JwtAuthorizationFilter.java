package com.example.forumproject.helpers;

import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.services.UserService;
import com.example.forumproject.services.securityServices.JwtService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public JwtAuthorizationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);
        String username = null;
        try {
            username = jwtService.extractUsername(token);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid token please login" + e.getMessage());
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {

                UserDetails userDetails = userService.loadUserByUsername(username);

                // check if user is blocked
                if (!userDetails.isEnabled()) {
                    throw new UnauthorizedAccessException("Your account is blocked!");
                }

                // Check if token is valid
                if (jwtService.isValid(token, userDetails)) {
                    // If token is valid, create authentication token and set it in the context
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    throw new UnauthorizedAccessException("No valid token provided! Try login again.");
                }
            } catch (UnauthorizedAccessException e) {
                // Token is invalid or expired or the user is blocked, return an error response directly from the filter
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: " + e.getMessage());
                return; // No further processing, response is already sent
            } catch (UsernameNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Your token is not valid!");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
