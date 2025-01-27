package com.example.forumproject.repositories.contracts;

import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.models.Post;

import java.util.List;

public interface PostRepository {

    Long getNumberOfPostsByUser(Long user_id);

    List<Post> getAllPostsFromUser(Long user_id);

    Long getTotalNumberOfPosts();

    List<Post> getAll(PostFilterOptions filterOptions);

    Post getById(Long id);

    void create(Post post);

    void update(Post post);

    void delete(Long id);

}
