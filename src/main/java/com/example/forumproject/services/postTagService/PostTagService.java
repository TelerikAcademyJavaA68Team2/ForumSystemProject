package com.example.forumproject.services.postTagService;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;

import java.util.List;

public interface PostTagService {

    List<Tag> getTagsByPostId(Long post_id);

    List<Post> getAllPostsByTagId(Long tag_id);

    boolean checkIfPostIsTagged(Long post_id, Long tag_id);

    void createTagOnPost(Long post_id, String tagName);

    void updateTagOnPost(Long postId, Long tagId, String newTagName);

    void deleteTagFromPost(Long post_id, Long tag_id);
}