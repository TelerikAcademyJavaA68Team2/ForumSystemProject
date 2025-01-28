package com.example.forumproject.services.reactionService;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;

import java.util.List;

public interface ReactionService {
    Long getLikesByPostId(Long post_id);

    Long getDislikesByPostId(Long post_id);

    List<Post> getAllLikedPosts(Long user_id);

    List<Post> getAllDislikedPosts(Long user_id);

    boolean save(Post post, User user, boolean like);
}
