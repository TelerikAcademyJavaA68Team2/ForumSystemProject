package com.example.forumproject.repositories.contracts;

import com.example.forumproject.models.Post;

import java.util.List;

public interface PostRepository {

    // List<Post> get(FilterOptions filterOptions);

    int getNumberOfPostsByUser(int user_id);

    List<Post> getAllPostsFromUser(int user_id);

    int getTotalNumberOfPosts();

    List<Post> getAll();

    Post getById(int id);

    void create(Post post);

    void update(Post post);

    void delete(int id);

}
