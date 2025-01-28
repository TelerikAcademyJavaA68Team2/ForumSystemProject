package com.example.forumproject.repositories.tagsRepository;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;

import java.util.List;

public interface TagRepository {

    boolean checkIfPostIsTagged(Long post_id, Long tag_id);

    Tag getTag(Long post_id, Long tag_id);

    List<Tag> getTagsByPostId(Long post_id);

    List<Post> getAllPostsByTag(Long tag_id);

    Tag getTagByTagId(Long tag_id);

    Tag getTagByName(String tagName);

    void create(Tag tag);

    void addTagToPost(Long postId, Long tagId);

    void update(Tag tag);

    void delete(Tag tag);
}