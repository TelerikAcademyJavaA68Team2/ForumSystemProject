package com.example.forumproject.repositories.tagsRepository;

import com.example.forumproject.models.Tag;

public interface TagRepository {

    Tag getTagById(Long tag_id);

    Tag getTagByName(String tagName);

    void create(Tag tag);

    void update(Tag tag);

    void delete(Tag tag);
}