package com.example.forumproject.services.contracts;


import com.example.forumproject.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    void save(User user);

    User getById(int userId);

    User getByEmail(String email);

    User getByUsername(String username);

    void deleteUser(int userId);

    void promoteToAdmin(int userId);

    void blockUser(int userId);

    void unblockUser(int userId);

    User loadUserByUsername(String username);

    User getAuthenticatedUser();
}
