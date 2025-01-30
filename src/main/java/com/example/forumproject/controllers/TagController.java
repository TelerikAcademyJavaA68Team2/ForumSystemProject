package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.models.Tag;
import com.example.forumproject.models.dtos.tagDtos.TagInDto;
import com.example.forumproject.services.postTagService.PostTagService;
import com.example.forumproject.services.tagService.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts/{postId}")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag Management",
        description = "API for managing tags associated with posts")
public class TagController {

    public static final String NO_TAGS_MESSAGE = "There are no tags for post with id %d!";
    private final TagService tagService;
    private final PostTagService postTagService;

    @Autowired
    public TagController(TagService tagService, PostTagService postTagService) {
        this.tagService = tagService;
        this.postTagService = postTagService;
    }

    @Operation(
            summary = "Get tags for a specific post",
            description = "Retrieve all tags associated with a given post",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved tags"),
                    @ApiResponse(responseCode = "404", description = "Post not found")
            }
    )
    @GetMapping("/tags")
    public List<String> getTagsForPost(@PathVariable Long postId) {
        try {
            List<String> tags = postTagService.getTagsByPostId(postId)
                    .stream()
                    .map(Tag::getTagName)
                    .toList();
            if(tags.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(NO_TAGS_MESSAGE, postId));
            }
            return tags;
        }  catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            summary = "Create a new tag for a post",
            description = "Add a new tag to a specific post",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created tag"),
                    @ApiResponse(responseCode = "404", description = "Post not found"),
                    @ApiResponse(responseCode = "409", description = "Duplicate tag name"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized to create tag")
            }
    )
    @PostMapping("/tags")
    public Tag addTagToPost(@Valid @RequestBody TagInDto tagDTO,
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

    @Operation(
            summary = "Update an existing tag for a post",
            description = "Modify the name of an existing tag associated with a specific post",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated tag"),
                    @ApiResponse(responseCode = "404", description = "Post or tag not found"),
                    @ApiResponse(responseCode = "409", description = "Duplicate tag name"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized to update tag")
            }
    )
    @PutMapping("/tags/{tagName}")
    public Tag updateTagInPostByTagId(@Valid @RequestBody TagInDto tagDTO,
                                      @PathVariable Long postId, @PathVariable String tagName) {
        try {
            Tag tag = tagService.getTagByName(tagName);
            postTagService.updateTagOnPost(postId, tag.getId(), tagDTO.getTagName());
            return tagService.getTagByName(tagDTO.getTagName());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(
            summary = "Delete a tag from a post by tag name",
            description = "Remove a tag from a specific post using the tag name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully deleted tag"),
                    @ApiResponse(responseCode = "404", description = "Post or tag not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized to delete tag")
            }
    )
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
}