package com.example.forumproject.services;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;

import java.util.List;

public interface PostService {

   // List<Post> get(FilterOptions filterOptions);

    List<Post> getAll();

    Post getById(int id);

    void create(Post beer, User user);

    void update(Post beer, User user);

    void delete(int id, User user);

}
