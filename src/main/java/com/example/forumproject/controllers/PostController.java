package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.mappers.PostMapper;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.postDtos.PostInDto;
import com.example.forumproject.models.dtos.postDtos.PostOutDto;
import com.example.forumproject.services.userService.UserService;
import com.example.forumproject.services.reactionService.ReactionService;
import com.example.forumproject.services.postService.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;
    private final ReactionService reactionService;

    @Autowired
    public PostController(PostService postService, PostMapper postMapper,
                          UserService userService, ReactionService reactionService) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.reactionService = reactionService;
        this.userService = userService;
    }

    @GetMapping("/posts")
    public List<PostOutDto> getAllPosts(@RequestParam(required = false) String title,
                                        @RequestParam(required = false) String content,
                                        @RequestParam(required = false) String tags,
                                        @RequestParam(required = false) Long minLikes,
                                        @RequestParam(required = false) Long maxLikes,
                                        @RequestParam(required = false) String orderBy,
                                        @RequestParam(required = false) String orderType) {


        PostFilterOptions filterOptions = new PostFilterOptions(title, content, tags, minLikes, maxLikes, orderBy, orderType, null);
        List<Post> inPosts = postService.getAll(filterOptions);
        return inPosts.stream().map(postMapper::postToPostOutDto).toList();
    }

//    //ToDo
//    @GetMapping("/10-most-commented")
//    public List<PostOutDto> getMostCommentedPosts() {
//        try {
//            List<Post> posts = postService.getMostCommentedPosts();
//            List<PostOutDto> result = new ArrayList<>();
//            posts.forEach(post -> result.add(postMapper.postToPostOutDto(post)));
//            return result;
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
//
//    //ToDo
//    @GetMapping("/10-most-recent")
//    public List<PostOutDto> getMostRecentlyCreatedPosts() {
//        try {
//            List<Post> posts = postService.getTenMostRecent();
//            List<PostOutDto> result = new ArrayList<>();
//            posts.forEach(post -> result.add(postMapper.postToPostOutDto(post)));
//            return result;
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }

    @GetMapping("/posts/{postId}")
    public PostOutDto getPostById(@PathVariable Long postId) {
        try {
            Post post = postService.getById(postId);
            return postMapper.postToPostOutDto(post);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException
                    (HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId) {
        try {
            User user = userService.getAuthenticatedUser();
            Post post = postService.getById(postId);
            String message = reactionService.save(post, user, true)
                    ? "Post LIKED successfully!" : "LIKE removed successfully!";
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/posts/{postId}/dislike")
    public ResponseEntity<String> dislikePost(@PathVariable Long postId) {
        try {
            User user = userService.getAuthenticatedUser();
            Post post = postService.getById(postId);
            String message = reactionService.save(post, user, false)
                    ? "Post DISLIKED successfully!" : "DISLIKE removed successfully!";
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/posts")
    public PostOutDto create(@Valid @RequestBody PostInDto postDto) { //todo maby otherDto with no id in? updatedto?
        try {
            User user = userService.getAuthenticatedUser();
            Post postToCreate = postMapper.createPostFromDto(postDto, user);
            postService.create(postToCreate, user);
            return postMapper.postToPostOutDto(postToCreate);
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    e.getMessage());
        }
    }

    @PutMapping("/posts/{postId}")
    public PostOutDto update(@PathVariable Long postId, @Valid @RequestBody PostInDto postDto) {
        try {
            User user = userService.getAuthenticatedUser();
            Post postToUpdate = postMapper.postInDtoToPost(postDto, postId);
            postService.update(postToUpdate, user);
            Post updatedPost = postService.getById(postId);
            return postMapper.postToPostOutDto(updatedPost);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        try {
            User user = userService.getAuthenticatedUser();
            postService.delete(postId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    e.getMessage());
        }
    }
}
