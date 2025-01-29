package com.example.forumproject.repositories;

import com.example.forumproject.models.User;
import com.example.forumproject.models.filterOptions.UsersFilterOptions;

import java.util.List;

public interface UserRepository {

    List<User> getAllUsers(UsersFilterOptions filterOptions);

    Long getNumberOfRegisteredUsers();

    User getById(Long userId);

    User getByUsername(String username);

    User getByEmail(String email);

    void save(User user);

    void deleteUser(User user);
}
