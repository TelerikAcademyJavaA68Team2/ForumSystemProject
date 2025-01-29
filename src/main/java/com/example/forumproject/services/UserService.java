package com.example.forumproject.services;

import com.example.forumproject.models.User;
import com.example.forumproject.models.filterOptions.UsersFilterOptions;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers(UsersFilterOptions filterOptions);

    Long getNumberOfRegisteredUsers();

    void save(User user);

    User getById(Long userId);

    User getByEmail(String email);

    User loadUserByUsername(String username);

    void deleteUser(Long userId);

    void promoteToAdmin(Long userId);

    void demoteAdminToUser(Long userId);

    void blockUser(Long userId);

    void unblockUser(Long userId);

    void updatePhoneNumber(User user, String phoneNumber);

    User getAuthenticatedUser();
}