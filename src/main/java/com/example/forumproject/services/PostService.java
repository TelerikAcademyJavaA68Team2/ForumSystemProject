package com.example.forumproject.services;

import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;

import java.util.List;

public interface PostService {

    List<Post> getAllPosts(PostFilterOptions filterOptions);

    Long getNumberOfPostsByUser(Long user_id);

    Long getTotalNumberOfPosts();

    Post getById(Long id);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(Long id, User user);
}