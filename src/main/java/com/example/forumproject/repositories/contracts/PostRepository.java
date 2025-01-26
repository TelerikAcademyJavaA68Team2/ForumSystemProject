package com.example.forumproject.repositories.contracts;

import com.example.forumproject.models.Post;

import java.util.List;

public interface PostRepository {

    // List<Post> get(FilterOptions filterOptions);

    List<Post> getAll();

    Post getById(int id);

    void create(Post post);

    void update(Post post);

    void delete(int id);

}
