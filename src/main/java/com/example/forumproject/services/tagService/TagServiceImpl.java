package com.example.forumproject.services.tagService;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;
import com.example.forumproject.repositories.tagsRepository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getTagsByPostId(Long post_id) {
        return tagRepository.getTagsByPostId(post_id);
    }
    @Override
    public boolean checkIfPostIsTagged(Long post_id, Long tag_id) {
        return tagRepository.checkIfPostIsTagged(post_id, tag_id);
    }

    @Override
    public Tag getOrCreateTag(String tagName) {
        tagName = tagName.toLowerCase(Locale.ROOT);
        try {
            return tagRepository.getTagByName(tagName);
        } catch (EntityNotFoundException e) {
            // If tag does not exist, create a new one
            Tag newTag = new Tag(tagName);
            tagRepository.create(newTag);
            return newTag;
        }
    }

    @Override
    public Tag getTag(Long post_id, Long tag_id) {
        return null;
    }

    @Override
    public Tag getTag(Long tag_id) {
        return null;
    }

    @Override
    public Tag getTagByTagId(Long tag_id) {
        return tagRepository.getTagByTagId(tag_id);
    }

    @Override
    public Tag getTagByName(String tagName) {
        return tagRepository.getTagByName(tagName);
    }
    @Override
    public List<Post> getAllPostsByTag(Long tag_id) {
        return tagRepository.getAllPostsByTag(tag_id);
    }

    @Override
    public void create(Tag tag) {
       // checkIfPostIsTagged()
        tagRepository.create(tag);
    }

    @Override
    public void update(Tag tag) {
        tagRepository.update(tag);
    }

    @Override
    public void delete(Tag tag) {
        tagRepository.delete(tag);
    }
}
