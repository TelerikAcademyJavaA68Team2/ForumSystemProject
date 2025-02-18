package com.example.forumproject.controllers.mvc;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.mappers.CommentMapper;
import com.example.forumproject.mappers.PostMapper;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.commentDtos.CommentInDto;
import com.example.forumproject.models.dtos.commentDtos.CommentOutDto;
import com.example.forumproject.models.dtos.postDtos.PostFilterDto;
import com.example.forumproject.models.dtos.postDtos.PostOutDto;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.UserService;
import com.example.forumproject.services.commentService.CommentService;
import com.example.forumproject.services.reactionService.ReactionService;
import com.example.forumproject.services.tagService.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Controller
@RequestMapping("/mvc/posts")
public class PostMvcController {

    private static final String REDIRECT = "redirect:/mvc/posts/%d";

    private final PostService postService;
    private final PostMapper postMapper;
    private final TagService tagService;
    private final UserService userService;
    private final ReactionService reactionService;
    private final CommentMapper commentMapper;
    private final CommentService commentService;

    public PostMvcController(PostService postService, PostMapper postMapper, TagService tagService, UserService userService, ReactionService reactionService, CommentMapper commentMapper, CommentService commentService) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.tagService = tagService;
        this.userService = userService;
        this.reactionService = reactionService;
        this.commentMapper = commentMapper;
        this.commentService = commentService;
    }

    @ModelAttribute("tags")
    public List<Tag> populateTags() {
        return tagService.getAllTags();
    }

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        try {
            return userService.getAuthenticatedUser();
        } catch (UnauthorizedAccessException e) {
            return null;
        }
    }

    @GetMapping
    public String showAllPosts(@ModelAttribute("filterOptions") PostFilterDto postFilterDto, Model model) {
        PostFilterOptions postFilterOptions = new PostFilterOptions(
                postFilterDto.getTitle(),
                postFilterDto.getContent(),
                postFilterDto.getTag(),
                postFilterDto.getMinLikes(),
                postFilterDto.getMaxLikes(),
                postFilterDto.getOrderBy(),
                postFilterDto.getOrderType(),
                postFilterDto.getAuthor());

        List<Post> postsObjects = postService.getAllPosts(postFilterOptions);

        List<PostOutDto> posts = postsObjects.stream()
                .map(postMapper::postToPostOutDto)
                .collect(Collectors.toList());

        model.addAttribute("filterOptions", postFilterDto);
        model.addAttribute("posts", posts);

        return "Posts-View";
    }

    @GetMapping("/{id}")
    public String showSinglePost(@PathVariable Long id, Model model) {

            Post currentPost = postService.getById(id);
            PostOutDto post = postMapper.postToPostOutDto(currentPost);
            model.addAttribute("post", post);
            model.addAttribute("comments", post.getComments());
            model.addAttribute("commentDto", new CommentOutDto());
            User currentUser;

            try {
                currentUser = userService.getAuthenticatedUser();
            } catch (UnauthorizedAccessException e) {
                currentUser = null;
            }

            if (currentUser != null) {
                boolean hasLiked = reactionService.checkIfLikeExists(id, currentPost.getId());
                boolean hasDisliked = reactionService.checkIfDislikeExists(id, currentPost.getId());

                model.addAttribute("hasLiked", hasLiked);
                model.addAttribute("hasDisliked", hasDisliked);
            } else {
                model.addAttribute("hasLiked", false);
                model.addAttribute("hasDisliked", false);
            }
            return "Post-View";
    }

    @GetMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        User user = userService.getAuthenticatedUser();
        postService.delete(id, user);


//        try {
//            postService.delete(id, user);
//        }catch (UnauthorizedOperationException u){
//            model.addAttribute("error", u.getMessage());
//            return "unauthorized";
//        }

        return "redirect:/mvc/posts";
    }

    @PostMapping("/{id}/comments/new")
    public String createComment(@PathVariable Long id, @Valid @ModelAttribute("commentDto") CommentInDto newComment,
                                BindingResult errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentError", "Comment cannot be empty!");
            return format(REDIRECT, id);
        }

        Comment comment = commentMapper.commentInDtoToObject(newComment);
        commentService.create(id, comment);
        redirectAttributes.addFlashAttribute("commentSuccess", "Comment added successfully!");
        return format(REDIRECT, id);
    }

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable Long id) {
        User user = userService.getAuthenticatedUser();
        Post post = postService.getById(id);
        reactionService.save(post, user, true);
        return format(REDIRECT, id);
    }

    @PostMapping("/{id}/dislike")
    public String dislikePost(@PathVariable Long id) {
        User user = userService.getAuthenticatedUser();
        Post post = postService.getById(id);
        reactionService.save(post, user, false);
        return format(REDIRECT, id);
    }


}
