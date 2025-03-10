package com.example.forumproject.helpers;

import com.example.forumproject.exceptions.*;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.userDtos.RequestUserProfileDto;
import com.example.forumproject.repositories.UserRepository;
import com.example.forumproject.services.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationHelpers {

    public static final String UNAUTHORIZED_MESSAGE_POST = "Only admins or the " +
            "post's creator can modify posts!";
    public static final String UNAUTHORIZED_MESSAGE_COMMENT = "Only admins or the " +
            "comment's creator can modify comments!";
    public static final String UNAUTHORIZED_MESSAGE_COMMENT_DELETE = "Only admins, the post creator or the " +
            "comment's creator can delete comments!";

    public static String ValidateUpdate(RequestUserProfileDto userUpdateDto, User user, UserService service,
                                        MultipartFile profileImage, CloudinaryHelper cloudinaryHelper) throws IOException {

        StringBuilder sb = new StringBuilder();

        boolean noChanges = true;
        if (userUpdateDto.getFirstName() != null) {
            noChanges = false;
            if (user.getFirstName().equals(userUpdateDto.getFirstName())) {
                throw new DuplicateEntityException("Your First Name is already set to " + user.getFirstName());
            }
            sb.append("First Name updated successfully!").append(System.lineSeparator());
            user.setFirstName(userUpdateDto.getFirstName());
        }
        if (userUpdateDto.getLastName() != null) {
            noChanges = false;
            if (user.getLastName().equals(userUpdateDto.getLastName())) {
                throw new DuplicateEntityException("Your Last Name is already set to " + user.getLastName());
            }
            sb.append("Last Name updated successfully!").append(System.lineSeparator());
            user.setLastName(userUpdateDto.getLastName());
        }
        if (userUpdateDto.getEmail() != null) {
            noChanges = false;
            if (user.getEmail().equals(userUpdateDto.getEmail())) {
                throw new DuplicateEntityException("Your Email is already set to " + user.getEmail());
            }
            boolean emailTaken = true;
            try {
                service.getByEmail(userUpdateDto.getEmail());
            } catch (EntityNotFoundException e) {
                emailTaken = false;
            }
            if (emailTaken) {
                throw new DuplicateEntityException("Email is already taken!");
            }
            sb.append("Email updated successfully!").append(System.lineSeparator());
            user.setEmail(userUpdateDto.getEmail());
        }
        if (userUpdateDto.getProfilePhoto() != null) {
            noChanges = false;

            if (!isValidImageFile(profileImage)) {
                throw new InvalidUserInputException("Invalid image file. Only JPEG, PNG, or GIF files are allowed.");
            }
            cloudinaryHelper.uploadUserProfilePhoto(profileImage, user);
            sb.append("Profile photo updated successfully!").append(System.lineSeparator());
        }
        if (userUpdateDto.getPhoneNumber() != null) {
            noChanges = false;
            if (!user.isAdmin()) {
                throw new UnauthorizedAccessException("You dont have permission to set your phone number!");
            }
            if (user.getPhoneNumber().equals(userUpdateDto.getPhoneNumber())) {
                throw new DuplicateEntityException("Your Phone number is already set to " + user.getPhoneNumber());
            }
            sb.append("Phone number updated successfully!").append(System.lineSeparator());
            user.setPhoneNumber(userUpdateDto.getPhoneNumber());
        }
        if (noChanges) {
            throw new InvalidUserInputException("You didn't provide valid patch request!");
        }
        return sb.toString();
    }

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

    public static boolean isValidImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg")
                || contentType.equals("image/png") || contentType.equals("image/gif"));
    }

    public static void validateUserIsAdminOrPostAuthor(Post post, User user) {
        if (!(user.isAdmin() || post.getAuthor().equals(user))) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_MESSAGE_POST);
        }
    }

    public static void validateUserIsAdminOrCommentAuthor(Comment comment, User user) {
        if (!(user.isAdmin() || comment.getAuthor().equals(user))) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_MESSAGE_COMMENT);
        }
    }

    public static void validateUserIsAdminOrCommentAuthorOrPostAuthor(Comment comment, Post post, User user) {
        if (!(user.isAdmin() || comment.getAuthor().equals(user) || post.getAuthor().equals(user))) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_MESSAGE_COMMENT_DELETE);
        }
    }

    public static boolean isDuplicatePost(Post newPost, Post postToUpdate) {
        return newPost.getContent().trim().equalsIgnoreCase(postToUpdate.getContent().trim()) &&
                newPost.getTitle().trim().equalsIgnoreCase(postToUpdate.getTitle().trim());
    }

    public static boolean isDuplicateComment(Comment newComment, Comment commentToUpdate) {
        return newComment.getContent().trim().equalsIgnoreCase(commentToUpdate.getContent().trim());
    }
}