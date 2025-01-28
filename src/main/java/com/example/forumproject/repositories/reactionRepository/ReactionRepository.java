package com.example.forumproject.repositories.reactionRepository;

import com.example.forumproject.models.Reaction;
import com.example.forumproject.models.Post;

import java.util.List;

public interface ReactionRepository {

    Reaction getLike(Long postId, Long userId);

    Long getLikesByPostId(Long post_id);

    Long getDislikesByPostId(Long post_id);

    boolean checkIfLikeExists(Long post_id, Long user_id);

    boolean checkIfDislikeExists(Long post_id, Long user_id);

    List<Post> getAllLikedPosts(Long user_id);

    List<Post> getAllDislikedPosts(Long user_id);

    void create(Reaction like);

    void update(Reaction like);

    void delete(Reaction like);
}
