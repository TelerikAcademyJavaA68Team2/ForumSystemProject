package com.example.forumproject.services.contracts;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.PostOutDto;

import java.util.List;

public interface PostService {

    // List<Post> get(FilterOptions filterOptions);

    int getNumberOfPostsByUser(int user_id);

    List<Post> getAllPostsFromUser(int user_id);

    int getTotalNumberOfPosts();

    List<Post> getAll();

    Post getById(int id);

    void create(Post beer, User user);

    void update(Post beer, User user);

    void delete(int id, User user);

}
