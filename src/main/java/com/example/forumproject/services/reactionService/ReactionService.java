package com.example.forumproject.services.reactionService;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;

public interface ReactionService {

    boolean checkIfLikeExists(Long post_id, Long user_id);

    boolean checkIfDislikeExists(Long post_id, Long user_id);

    Long getLikesByPostId(Long post_id);

    Long getDislikesByPostId(Long post_id);

    boolean save(Post post, User user, boolean like);
}