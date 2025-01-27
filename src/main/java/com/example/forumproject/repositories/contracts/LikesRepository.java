package com.example.forumproject.repositories.contracts;

import com.example.forumproject.models.Like;
import com.example.forumproject.models.Post;

import java.util.List;

public interface LikesRepository {

    Like getLike(Long postId, Long userId);

    Long getLikesByPostId(Long post_id);

    Long getDislikesByPostId(Long post_id);

    boolean checkIfLikeExists(Long post_id, Long user_id);

    boolean checkIfDislikeExists(Long post_id, Long user_id);

    List<Post> getAllLikedPosts(Long user_id);

    List<Post> getAllDislikedPosts(Long user_id);

    void create(Like like);

    void update(Like like);

    void delete(Like like);
}
