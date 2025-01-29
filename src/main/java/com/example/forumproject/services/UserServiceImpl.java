package com.example.forumproject.services;

import com.example.forumproject.exceptions.InvalidUserInputException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.helpers.ValidationHelpers;
import com.example.forumproject.models.User;
import com.example.forumproject.models.filterOptions.UsersFilterOptions;
import com.example.forumproject.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers(UsersFilterOptions filterOptions) {
        return userRepository.getAllUsers(filterOptions);
    }

    @Override
    public Long getNumberOfRegisteredUsers() {
        return userRepository.getNumberOfRegisteredUsers();
    }

    @Override
    public void save(User user) {
        ValidationHelpers.validateEmailAndUsername(user, userRepository);
        userRepository.save(user);
    }

    @Override
    public User getById(Long userId) {
        return userRepository.getById(userId);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.getById(userId);
        userRepository.deleteUser(user);
    }

    @Override
    public void promoteToAdmin(Long userId) {
        User user = getById(userId);
        if (user.isAdmin()) {
            throw new InvalidUserInputException(String.format(
                    "Admin with id: %d is already Admin! To demote him to User try /make-user", userId));
        }
        user.setAdmin(true);
        userRepository.save(user);
    }

    @Override
    public void demoteAdminToUser(Long userId) {
        User user = getById(userId);
        if (!user.isAdmin()) {
            throw new InvalidUserInputException(String.format("User with id: %d is already User!", userId));
        }
        user.setAdmin(false);
        userRepository.save(user);
    }

    @Override
    public void blockUser(Long userId) {
        User user = userRepository.getById(userId);
        if (user.isBlocked()) {
            throw new InvalidUserInputException(String.format(
                    "User with id: %d is already blocked! To unblock him try /unblock", userId));
        }
        user.setBlocked(true);
        userRepository.save(user);
    }

    @Override
    public void unblockUser(Long userId) {
        User user = getById(userId);
        if (!user.isBlocked()) {
            throw new InvalidUserInputException(String.format
                    ("User with id: %d is not currently blocked!", userId));
        }
        user.setBlocked(false);
        userRepository.save(user);
    }

    @Override
    public void updatePhoneNumber(String phoneNumber) {
        ValidationHelpers.validatePhoneNumber(phoneNumber);
        User user = getAuthenticatedUser();
        if (user.getPhoneNumber() != null && user.getPhoneNumber().equals(phoneNumber)) {
            throw new InvalidUserInputException("You provided the phone number that's already in your profile info!");
        }
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }

    public User getAuthenticatedUser() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user == null) {
                throw new EntityNotFoundException("User not found!");
            }
            return user;
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Please login first!");
        }
    }
}