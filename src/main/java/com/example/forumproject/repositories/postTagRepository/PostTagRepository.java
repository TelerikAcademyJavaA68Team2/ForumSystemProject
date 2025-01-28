package com.example.forumproject.repositories.postTagRepository;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.PostTag;
import com.example.forumproject.models.Tag;

import java.util.List;

public interface PostTagRepository {

    List<Tag> getTagsByPostId(Long post_id);

    List<Post> getAllPostsByTagId(Long tag_id);

    boolean checkIfPostIsTagged(Long post_id, Long tag_id);

    PostTag getPostTag(Long postId, Long tagId);

    void create(PostTag postTag);

    void update(PostTag postTag, Tag newTag);

    void delete(PostTag postTag);
}

