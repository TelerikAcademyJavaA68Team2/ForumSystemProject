package com.example.forumproject.services.tagService;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.Tag;
import com.example.forumproject.repositories.tagsRepository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.getAllTags();
    }

    @Override
    public Tag getTagById(Long tag_id) {
        return tagRepository.getTagById(tag_id);
    }

    @Override
    public Tag getTagByName(String tagName) {
        try {
            return tagRepository.getTagByName(tagName);
        } catch (EntityNotFoundException e) {
            tagRepository.create(new Tag(tagName));
        }
        return tagRepository.getTagByName(tagName);
    }
}
