package com.example.forumproject.helpers;

import com.example.forumproject.exceptions.*;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidationHelpers {

    private static final String MODIFY_POSTS = "Only admins or the post's creator can modify posts!";
    private static final String MODIFY_COMMENTS = "Only admins or the comment's creator can modify comments!";

    public static void validateEmailAndUsername(User user, UserRepository userRepository) {
        boolean usernameIsNotValid = true;
        try {
            userRepository.getByUsername(user.getUsername());
        } catch (UsernameNotFoundException e) {
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

    public static void validateUserIsAdminOrPostAuthor(Post post, User user) {
        if (!(user.isAdmin() || post.getAuthor().equals(user))) {
            throw new UnauthorizedAccessException(MODIFY_POSTS);
        }
    }

    public static void validateUserIsAdminOrCommentAuthor(Comment comment, User user) {
        if (!(user.isAdmin() || comment.getAuthor().equals(user))) {
            throw new UnauthorizedAccessException(MODIFY_COMMENTS);
        }
    }


    public static boolean isDuplicatePost(Post newPost, Post postToUpdate) {
        return newPost.getContent().trim().equalsIgnoreCase(postToUpdate.getContent().trim()) &&
                newPost.getTitle().trim().equalsIgnoreCase(postToUpdate.getTitle().trim());
    }

    public static boolean isDuplicateComment(Comment newComment, Comment commentToUpdate) {
        return newComment.getContent().trim().equalsIgnoreCase(commentToUpdate.getContent().trim());
    }

    public static void validatePhoneNumber(String phoneNumber) {
        String regex = "^[\\d+\\- ]{6,20}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        if (!matcher.matches()) {
            throw new InvalidUserInputException("You provided wrong phone number -> 6-20 chars allowed symbols -> {+/{space}/-}");
        }
    }
}
