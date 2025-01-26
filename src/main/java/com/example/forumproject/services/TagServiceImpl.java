package com.example.forumproject.services;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;
import com.example.forumproject.repositories.TagRepository;
import com.example.forumproject.services.contracts.TagService;
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
    public List<Tag> getTagsByPostId(int post_id) {
        return tagRepository.getTagsByPostId(post_id);
    }

    @Override
    public List<Post> getAllPostsByTag(int tag_id) {
        return tagRepository.getAllPostsByTag(tag_id);
    }

    @Override
    public void create(Tag tag) {
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
