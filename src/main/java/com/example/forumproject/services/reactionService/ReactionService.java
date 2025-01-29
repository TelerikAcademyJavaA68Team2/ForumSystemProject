package com.example.forumproject.services.reactionService;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;

public interface ReactionService {
    Long getLikesByPostId(Long post_id);

    Long getDislikesByPostId(Long post_id);

    boolean save(Post post, User user, boolean like);
}