package com.example.forumproject.services;

import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;

import java.util.List;

public interface PostService {

    List<Post> getAll(PostFilterOptions filterOptions);

    Long getNumberOfPostsByUser(Long user_id);

    Long getTotalNumberOfPosts();

    Post getById(Long id);

    void create(Post beer, User user);

    void update(Post beer, User user);

    void delete(Long id, User user);

}
