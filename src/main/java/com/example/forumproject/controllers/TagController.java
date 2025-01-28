package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.mappers.TagMapper;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.tagDtos.TagInDto;
import com.example.forumproject.services.postService.PostService;
import com.example.forumproject.services.postTagService.PostTagService;
import com.example.forumproject.services.tagService.TagService;
import com.example.forumproject.services.userService.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts/{postId}/tags")
public class TagController {

    private final TagService tagService;
    private final PostService postService;
    private final UserService userService;
    private final TagMapper tagMapper;
    private final PostTagService postTagService;

    @Autowired
    public TagController(TagService tagService, PostService postService, UserService userService, TagMapper tagMapper, PostTagService postTagService) {
        this.tagService = tagService;
        this.postService = postService;
        this.userService = userService;
        this.tagMapper = tagMapper;
        this.postTagService = postTagService;
    }

    // Get all tags for a specific post
    @GetMapping
    public List<String> getTagsForPost(@PathVariable Long postId) {
        try {
            return tagService.getTagsByPostId(postId)
                    .stream()
                    .map(tagMapper::TagToTagName)
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Add a tag to a post
    @PostMapping
    public Tag createTagInPost(@Valid @RequestBody TagInDto tagDTO,
                               @PathVariable Long postId) {
        try {
            Tag tag = tagMapper.TagInDtoToTag(tagDTO);
            Post post = postService.getById(postId);
            User user = userService.getAuthenticatedUser();
            postTagService.addTagToPost(post, tag);
            return tagService.getTagByName(tag.getTagName());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Remove a tag from a post
    @DeleteMapping("/{tagName}")
    public void deleteTagFromPost(@PathVariable Long postId,
                                  @PathVariable String tagName) {
        try {
            User user = userService.getAuthenticatedUser();
           // postService.removeTagFromPost(tagName, user, postId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

//        @GetMapping("/posts/{postId}/tags")
//
//    @PostMapping("/posts/{postId}/tags")
//    public Tag createTagInPost(@Valid @RequestBody TagInDto tagDTO,
//                               @PathVariable Long postId) {
//        try {
//            Tag tag = tagMapper.TagInDtoToTag(tagDTO);
//            // tagService checks in tag repository if tag with tag_name exists
//            // returns true or false
//            //if exists/true add the existing tag to the post
//            //else create tag and add to the post and database
//            User user = userService.getAuthenticatedUser();
//            postService.createTagInPost(tag, user, postId);
//            return tagService.getByName(tag.getTagName());
//        } catch (DuplicateEntityException e) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
//        } catch (UnauthorizedAccessException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/posts/{postId}/tags/")
}