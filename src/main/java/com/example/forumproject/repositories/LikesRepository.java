package com.example.forumproject.repositories;

import com.example.forumproject.models.Like;
import com.example.forumproject.models.Post;

import java.util.List;

public interface LikesRepository {

    Like getLike(int postId, int userId);

    int getLikesByPostId(int post_id);

    int getDislikesByPostId(int post_id);

    boolean checkIfLikeExists(int post_id, int user_id);

    boolean checkIfDislikeExists(int post_id, int user_id);

    List<Post> getAllLikedPosts(int user_id);

    List<Post> getAllDislikedPosts(int user_id);

    void create(Like like);

    void update(Like like);

    void delete(Like like);
}
