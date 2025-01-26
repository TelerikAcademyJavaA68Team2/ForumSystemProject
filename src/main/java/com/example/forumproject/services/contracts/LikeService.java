package com.example.forumproject.services.contracts;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;

import java.util.List;

public interface LikeService {
    int getLikesByPostId(int post_id);

    int getDislikesByPostId(int post_id);

    List<Post> getAllLikedPosts(int user_id);

    List<Post> getAllDislikedPosts(int user_id);

    boolean save(Post post, User user, boolean like);
}
