package com.example.forumproject.repositories.tagsRepository;

import com.example.forumproject.models.Tag;

import java.util.List;

public interface TagRepository {

    List<Tag> getAllTags();

    Tag getTagById(Long tag_id);

    Tag getTagByName(String tagName);

    void create(Tag tag);

    void update(Tag tag);

    void delete(Tag tag);
}