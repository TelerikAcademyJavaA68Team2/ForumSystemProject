package com.example.forumproject.services.tagService;

import com.example.forumproject.models.Tag;

import java.util.List;

public interface TagService {

    List<Tag> getAllTags();

    Tag getTagById(Long tag_id);

    Tag getOrCreateAndGetTagByName(String tagName);
}
