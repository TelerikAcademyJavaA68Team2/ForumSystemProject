package com.example.forumproject.services.implementation;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.contracts.PostRepository;
import com.example.forumproject.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.forumproject.helpers.ValidationHelpers.*;

@Service
public class PostServiceImpl implements PostService {

    public static final String DUPLICATE_POST_MESSAGE = "The post already has the same title and content";

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getAll(){
        return postRepository.getAll();
    }

    @Override
    public Post getById(int id) {
        return postRepository.getById(id);
    }

    @Override
    public void create(Post post, User user) {
        validateUserIsNotBlocked(user);
        postRepository.create(post);
    }

    @Override
    public void update(Post newPost, User user) {
        validateUserIsNotBlocked(user);
        Post postToUpdate = postRepository.getById(newPost.getId());
        validateUserIsAdminOrPostAuthor(postToUpdate, user);
        if (isDuplicatePost(newPost, postToUpdate)){
            throw new DuplicateEntityException(DUPLICATE_POST_MESSAGE);
        }
        postToUpdate.setTitle(newPost.getTitle());
        postToUpdate.setContent(newPost.getContent());
        postRepository.update(postToUpdate);
    }

    @Override
    public void delete(int id, User user) {
        validateUserIsNotBlocked(user);
        Post postToDelete = postRepository.getById(id);
        validateUserIsAdminOrPostAuthor(postToDelete, user);
        postRepository.delete(id);
    }


}
