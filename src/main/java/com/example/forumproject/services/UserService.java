package com.example.forumproject.services;


import com.example.forumproject.models.entitys.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getUserById(int id);

    User getByEmail(String email);

    void create(User user);

    void update(User user, int id);

    void delete(int id);
}
