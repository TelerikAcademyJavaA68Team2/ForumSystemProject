package com.example.forumproject.services.tagService;

import com.example.forumproject.models.Tag;
import com.example.forumproject.repositories.tagsRepository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag getTagById(Long tag_id) {
        return tagRepository.getTagById(tag_id);
    }

    @Override
    public Tag getTagByName(String tagName) {
        return tagRepository.getTagByName(tagName);
    }
}
