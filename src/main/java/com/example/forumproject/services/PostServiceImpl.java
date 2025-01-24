package com.example.forumproject.services;

import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    public static final String MODIFY_POSTS = "Only admins or the post's creator can modify posts!";
    private static final String BLOCKED_USER_ERROR_MESSAGE =
            "Your account is currently blocked! Your permissions to create or edit posts are restricted!";

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
        post.setAuthor(user);
        postRepository.create(post);
    }

    @Override
    public void update(Post post, User user) {
        Post postToUpdate = postRepository.getById(post.getId());
        validateUserIsNotBlocked(user);
        validateUserIsAdminOrAuthor(postToUpdate.getId(), user);
        postRepository.update(postToUpdate);
    }

    @Override
    public void delete(int id, User user) {
        validateUserIsNotBlocked(user);
        validateUserIsAdminOrAuthor(id, user);
        postRepository.delete(id);
    }

    private void validateUserIsAdminOrAuthor(int postId, User user) {
        Post post = postRepository.getById(postId);
        if (!(user.isAdmin() || post.getAuthor().equals(user))) {
            throw new UnauthorizedAccessException(MODIFY_POSTS);
        }
    }

    private static void validateUserIsNotBlocked(User user) {
        if (user.isBlocked()) {
            throw new UnauthorizedAccessException(BLOCKED_USER_ERROR_MESSAGE);
        }
    }

}
