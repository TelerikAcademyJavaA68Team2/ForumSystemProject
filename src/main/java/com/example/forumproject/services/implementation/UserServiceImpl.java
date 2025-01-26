package com.example.forumproject.services.implementation;

import com.example.forumproject.helpers.ValidationHelpers;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.contracts.UserRepository;
import com.example.forumproject.services.contracts.UserService;
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
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public void save(User user) {
        ValidationHelpers.validateEmailAndUsername(user, userRepository);

        userRepository.save(user);
    }

    @Override
    public User getById(int userId) {
        return userRepository.getById(userId);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.deleteUser(userId);
    }

    @Override
    public void promoteToAdmin(int userId) {
        User user = getById(userId);
        user.setAdmin(true);
        userRepository.save(user);
    }

    @Override
    public void blockUser(int userId) {
        User user = getById(userId);
        user.setBlocked(true);
        userRepository.save(user);
    }

    @Override
    public void unblockUser(int userId) {
        User user = getById(userId);
        user.setBlocked(false);
        userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getByUsername(username);
    }

    @Override
    public User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
