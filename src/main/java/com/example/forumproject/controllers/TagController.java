package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.mappers.TagMapper;
import com.example.forumproject.models.Tag;
import com.example.forumproject.models.dtos.tagDtos.TagInDto;
import com.example.forumproject.services.postTagService.PostTagService;
import com.example.forumproject.services.tagService.TagService;
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

    @PostMapping("/tags")
    public Tag createTagInPost(@Valid @RequestBody TagInDto tagDTO,
                               @PathVariable Long postId) {
        try {
            String newTagName = tagDTO.getTagName();
            postTagService.createTagOnPost(postId, newTagName);
            return tagService.getTagByName(newTagName);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/tags/{tagId}")
    public Tag updateTagInPostByTagId(@Valid @RequestBody TagInDto tagDTO,
                                      @PathVariable Long postId, @PathVariable Long tagId) {
        try {
            postTagService.updateTagOnPost(postId, tagId, tagDTO.getTagName());
            return tagService.getTagByName(tagDTO.getTagName());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
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
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

 /*   @DeleteMapping("/tags/{tagId}")
    public void deleteTagFromPost(@PathVariable Long postId,
                                  @PathVariable Long tagId) {
        try {
            postTagService.deleteTagFromPost(postId, tagId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }*/
}