package com.example.forumproject.repositories.likesDislikeRepo;

import com.example.forumproject.models.PostLikesDislikes;
import com.example.forumproject.models.Post;

import java.util.List;

public interface LikeDislikeRepository {

    PostLikesDislikes getLike(Long postId, Long userId);

    Long getLikesByPostId(Long post_id);

    Long getDislikesByPostId(Long post_id);

    boolean checkIfLikeExists(Long post_id, Long user_id);

    boolean checkIfDislikeExists(Long post_id, Long user_id);

    List<Post> getAllLikedPosts(Long user_id);

    List<Post> getAllDislikedPosts(Long user_id);

    void create(PostLikesDislikes like);

    void update(PostLikesDislikes like);

    void delete(PostLikesDislikes like);
}
