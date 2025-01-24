package com.example.forumproject.services;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService{

    private static final String BLOCKED_CREATION = "Blocked users can't create posts!";

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post getById(int id) {
        return postRepository.getById(id);
    }

    @Override
    public void create(Post post, User user) {

        if (user.isBlocked()){
            throw new UnauthorizedAccessException(BLOCKED_CREATION);
        }

        post.setAuthor(user);
        postRepository.create(post);
    }

    @Override
    public void update(Post post, User user) {

    }

    @Override
    public void delete(int id, User user) {

    }
}
