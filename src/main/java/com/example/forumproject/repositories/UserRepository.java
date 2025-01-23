package com.example.forumproject.repositories;

import com.example.forumproject.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAllUsers();

    void createUser(User user);

    User getById(int userId);

    User getByEmail(String email);

    User getByUsername(String username);

    void updateUser(User user);

    void deleteUser(int userId);

    void promoteToAdmin(int userId);

    void blockUser(int userId);

    void unblockUser(int userId);
}
