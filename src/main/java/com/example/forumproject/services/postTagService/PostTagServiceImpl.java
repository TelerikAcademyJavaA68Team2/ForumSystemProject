package com.example.forumproject.services.postTagService;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.helpers.ValidationHelpers;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.PostTag;
import com.example.forumproject.models.Tag;
import com.example.forumproject.repositories.postRepository.PostRepository;
import com.example.forumproject.repositories.postTagRepository.PostTagRepository;
import com.example.forumproject.repositories.tagsRepository.TagRepository;
import com.example.forumproject.services.userService.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostTagServiceImpl implements PostTagService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final UserService userService;

    @Autowired
    public PostTagServiceImpl(PostRepository postRepository, TagRepository tagRepository, PostTagRepository postTagRepository, UserService userService) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postTagRepository = postTagRepository;
        this.userService = userService;

    }

    @Override
    public List<Tag> getTagsByPostId(Long post_id) {
        postRepository.getById(post_id);
        return postTagRepository.getTagsByPostId(post_id);
    }

    @Override
    public List<Post> getAllPostsByTagId(Long tag_id) {
        return postTagRepository.getAllPostsByTagId(tag_id);
    }

    @Override
    public boolean checkIfPostIsTagged(Long post_id, Long tag_id) {
        return postTagRepository.checkIfPostIsTagged(post_id, tag_id);
    }

    @Transactional
    public void createTagOnPost(Long post_id, String tagName) {
        Post post = postRepository.getById(post_id);
        ValidationHelpers.validateUserIsAdminOrPostAuthor(post, userService.getAuthenticatedUser());

        Tag tag;
        try {
            tag = tagRepository.getTagByName(tagName);
        } catch (Exception e) {
            tag = new Tag();
            tag.setTagName(tagName);
            tagRepository.create(tag);
        }
        if (postTagRepository.checkIfPostIsTagged(post.getId(), tag.getId())) {
            throw new DuplicateEntityException("This tag is already associated with the post");
        }

        PostTag postTag = new PostTag(post, tag);
        postTagRepository.create(postTag);
    }

    @Transactional
    public void updateTagOnPost(Long postId, Long oldTagId, String newTagName) {
        Post post = postRepository.getById(postId);
        ValidationHelpers.validateUserIsAdminOrPostAuthor(post, userService.getAuthenticatedUser());
        Tag oldTag = tagRepository.getTagById(oldTagId);
        PostTag postTag = postTagRepository.getPostTag(postId, oldTagId);

        Tag tag;
        try {
            tag = tagRepository.getTagByName(newTagName);
        } catch (Exception e) {
            tag = new Tag();
            tag.setTagName(newTagName);
            tagRepository.create(tag);
        }
        if (postTagRepository.checkIfPostIsTagged(postId, tag.getId())) {
            throw new DuplicateEntityException("You cant update the tag to existing tag for the current post");
        }

        postTagRepository.update(postTag, tag);
    }

    @Transactional
    public void deleteTagFromPost(Long post_id, Long tag_id) {
        Post post = postRepository.getById(post_id);
        Tag tag = tagRepository.getTagById(post_id);
        postTagRepository.checkIfPostIsTagged(post_id, tag_id);
        ValidationHelpers.validateUserIsAdminOrPostAuthor(post, userService.getAuthenticatedUser());

        postTagRepository.delete(postTagRepository.getPostTag(post_id, tag_id));
    }
}

