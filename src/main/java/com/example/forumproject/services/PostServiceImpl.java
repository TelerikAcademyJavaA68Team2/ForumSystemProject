package com.example.forumproject.services;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.forumproject.helpers.ValidationHelpers.isDuplicatePost;
import static com.example.forumproject.helpers.ValidationHelpers.validateUserIsAdminOrPostAuthor;

@Service
public class PostServiceImpl implements PostService {

    public static final String DUPLICATE_POST_MESSAGE = "The post already has the same title and content";

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Long getNumberOfPostsByUser(Long user_id) {
        return postRepository.getNumberOfPostsByUser(user_id);
    }

  /*  @Override
    public List<Post> getAllPostsFromUser(Long user_id) {
        return postRepository.getAllPostsFromUser(user_id);
    }*/

    @Override
    public Long getTotalNumberOfPosts() {
        return postRepository.getTotalNumberOfPosts();
    }

    @Override
    public List<Post> getAll(PostFilterOptions filterOptions) {
        return postRepository.getAll(filterOptions);
    }

    @Override
    public Post getById(Long id) {
        return postRepository.getById(id);
    }

    @Override
    public void create(Post post, User user) {
        postRepository.create(post);
    }

    @Override
    public void update(Post newPost, User user) {
        Post postToUpdate = postRepository.getById(newPost.getId());
        validateUserIsAdminOrPostAuthor(postToUpdate, user);
        if (isDuplicatePost(newPost, postToUpdate)) {
            throw new DuplicateEntityException(DUPLICATE_POST_MESSAGE);
        }
        postToUpdate.setTitle(newPost.getTitle());
        postToUpdate.setContent(newPost.getContent());
        postRepository.update(postToUpdate);
    }

    @Override
    public void delete(Long id, User user) {
        Post postToDelete = postRepository.getById(id);
        validateUserIsAdminOrPostAuthor(postToDelete, user);
        postRepository.delete(id);
    }


}
