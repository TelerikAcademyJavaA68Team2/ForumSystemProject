package com.example.forumproject.services.tagService;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;

import java.util.List;

public interface TagService {

    //probably wont need these
    boolean checkIfPostIsTagged(Long post_id, Long tag_id);

    Tag getTag(Long post_id, Long tag_id);

    Tag getTag(Long tag_id);

    Tag getTagByTagId(Long tag_id);

    Tag getTagByName(String tagName);

    Tag getOrCreateTag(String tagName);

    List<Tag> getTagsByPostId(Long post_id);

    List<Post> getAllPostsByTag(Long tag_id);

    void create(Tag tag);

    void update(Tag tag);

    void delete(Tag tag);
}
