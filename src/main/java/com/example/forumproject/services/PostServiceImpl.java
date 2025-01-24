package com.example.forumproject.services;

import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    private static final String BLOCKED_CREATION = "Blocked users can't create posts!";
    public static final String MODIFY_POSTS = "Only admins or the post's creator can modify posts!";

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

        if (user.isBlocked()) {
            throw new UnauthorizedAccessException(BLOCKED_CREATION);
        }

        post.setAuthor(user);
        postRepository.create(post);
    }

    @Override
    public void update(Post post, User user) {
        checkModifyPermissions(post.getId(), user);


    }

    @Override
    public void delete(int id, User user) {
        checkModifyPermissions(id, user);
        postRepository.delete(id);
    }

    public void checkModifyPermissions(int postId, User user) {
        Post post = postRepository.getById(postId);
        if (!(user.isAdmin() || post.getAuthor().equals(user))) {
            throw new UnauthorizedAccessException(MODIFY_POSTS);
        }
    }
}
