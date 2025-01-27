/*
package com.example.forumproject.helpers;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.models.User;
import com.example.forumproject.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String INVALID_AUTHENTICATION = "Invalid authentication";

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (headers == null || !headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new UnauthorizedAccessException(INVALID_AUTHENTICATION);
        }

        try {
            String credentials = headers.getFirst(AUTHORIZATION_HEADER_NAME);

            if (credentials == null || credentials.isBlank()) {
                throw new UnauthorizedAccessException(INVALID_AUTHENTICATION);
            }

            String[] userParameters = credentials.split(" ");
            if (userParameters.length != 2) {
                throw new UnauthorizedAccessException(INVALID_AUTHENTICATION);
            }

            String username = userParameters[0];
            String password = userParameters[1];
            User userToCheck = userService.loadUserByUsername(username);

            if (userToCheck == null
                    || userToCheck.getPassword() == null
                    || !userToCheck.getPassword().equals(password)) {
                throw new UnauthorizedAccessException(INVALID_AUTHENTICATION);
            }
            return userToCheck;

        } catch (EntityNotFoundException e) {
            throw new UnauthorizedAccessException(INVALID_AUTHENTICATION);
        }


    }
}

*/
