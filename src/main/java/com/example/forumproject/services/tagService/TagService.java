package com.example.forumproject.services.tagService;

import com.example.forumproject.models.Tag;

public interface TagService {

    Tag getTagById(Long tag_id);

    Tag getTagByName(String tagName);
}
