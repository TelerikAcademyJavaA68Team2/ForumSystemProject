package com.example.forumproject.helpers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.InvalidEmailFormatException;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidationHelpers {

    public static void validateEmailAndUsername(User user, UserRepository userRepository) {
        boolean usernameIsNotValid = true;
        try {
            userRepository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            usernameIsNotValid = false;
        }
        if (usernameIsNotValid) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }

        boolean emailIsNotValid = true;
        try {
            userRepository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            emailIsNotValid = false;
        }
        if (emailIsNotValid) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
        checkEmail(user.getEmail());
    }

    private static void checkEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new InvalidEmailFormatException(email);
        }
    }
}
