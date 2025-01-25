package com.example.forumproject.repositories;

import com.example.forumproject.models.User;

import java.util.List;

public interface UserRepository{

    List<User> getAllUsers();

    User getById(int userId);

    User getByUsername(String username);

    User getByEmail(String email);

    void save(User user);

    void deleteUser(int userId);

    void promoteToAdmin(int userId);

    void blockUser(int userId);

    void unblockUser(int userId);
}
