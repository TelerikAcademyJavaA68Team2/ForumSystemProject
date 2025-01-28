package com.example.forumproject.services.postTagService;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.PostTag;
import com.example.forumproject.models.Tag;
import com.example.forumproject.repositories.postTagRepository.PostTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostTagServiceImpl implements PostTagService {

    private final PostTagRepository postTagRepository;

    @Autowired
    public PostTagServiceImpl(PostTagRepository postTagRepository) {
        this.postTagRepository = postTagRepository;
    }

    @Override
    public void addTagToPost(Post post, Tag tag) {
        PostTag postTag = new PostTag(post, tag);
        postTagRepository.addPostTag(postTag);
    }
}

