package com.example.forumproject.helpers;

import com.example.forumproject.exceptions.AuthenticationFailureException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.User;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    public static final String AUTHENTICATION_FAILURE_MESSAGE = "Wrong username or password";

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }


    public User tryGetUser(HttpSession session){
        String currentUser = (String) session.getAttribute("currentUser");
        if (currentUser == null){
            throw new AuthenticationFailureException("No user logged in!");
        }
        return userService.loadUserByUsername(currentUser);
    }

    public User verifyAuthentication(String username, String password){
        try{
            User user = userService.loadUserByUsername(username);
            if (!user.getPassword().equals(password)) {
                throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
            }
            return user;
        } catch (EntityNotFoundException e){
            throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
        }
    }
}
