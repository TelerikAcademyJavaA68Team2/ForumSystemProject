package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.helpers.ValidationHelpers;
import com.example.forumproject.mappers.TagMapper;
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
@RequestMapping("/api/posts/{postId}")
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;
    private final PostTagService postTagService;

    @Autowired
    public TagController(TagService tagService, TagMapper tagMapper, PostTagService postTagService) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
        this.postTagService = postTagService;
    }

    // Get all tags for a specific post
    @GetMapping("/tags")
    public List<String> getTagsForPost(@PathVariable Long postId) {
        try {
            return postTagService.getTagsByPostId(postId)
                    .stream()
                    .map(tagMapper::TagToTagName)
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Add a tag to a post
    @PostMapping("/tags")
    public Tag createTagInPost(@Valid @RequestBody TagInDto tagDTO,
                               @PathVariable Long postId) {
        try {
            String newTagName = tagDTO.getTagName();
            postTagService.createTagOnPost(postId, newTagName);
            return tagService.getTagByName(newTagName);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/tags/{tagId}")
    public Tag updateTagInPostByTagId(@Valid @RequestBody TagInDto tagDTO,
                                      @PathVariable Long postId, @PathVariable Long tagId) {
        try {
            Tag newTag = new Tag(tagDTO.getTagName().toLowerCase());
            postTagService.updateTagOnPost(postId, tagId, newTag);
            return tagService.getTagByName(newTag.getTagName());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @DeleteMapping("/tags/{tagName}")
    public void deleteTagFromPost(@PathVariable Long postId,
                                  @PathVariable String tagName) {
        try {
            Tag tag = tagService.getTagByName(tagName);
            postTagService.deleteTagFromPost(postId, tag.getId());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/tags/{tagId}")
    public void deleteTagFromPost(@PathVariable Long postId,
                                  @PathVariable Long tagId) {
        try {
            postTagService.deleteTagFromPost(postId, tagId);
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