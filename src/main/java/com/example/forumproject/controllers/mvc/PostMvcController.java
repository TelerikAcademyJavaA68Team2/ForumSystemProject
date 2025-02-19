package com.example.forumproject.controllers.mvc;

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
import com.example.forumproject.models.dtos.postDtos.PostInDto;
import com.example.forumproject.models.dtos.postDtos.PostOutDto;
import com.example.forumproject.models.dtos.postDtos.PostUpdateDto;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.UserService;
import com.example.forumproject.services.commentService.CommentService;
import com.example.forumproject.services.reactionService.ReactionService;
import com.example.forumproject.services.tagService.TagService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("tags")
    public List<Tag> populateTags() {
        return tagService.getAllTags();
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
    @GetMapping("/new")
    public String showCreatePostPage(Model model) {
        User user = userService.getAuthenticatedUser();

        //ToDo what happends when a user is blocked
//        if(user.isBlocked()){
//            model.addAttribute("error", "Blocked users can't create posts.");
//            return "unauthorized";
//        }

        model.addAttribute("author", user.getUsername());
        model.addAttribute("action", "create");
        model.addAttribute("post", new PostInDto());
        return "Post-New-View";
    }

    @PostMapping("/new")
    public String createPost(@Valid @ModelAttribute("post") PostInDto createPostDTO,
                             BindingResult errors, Model model) {
        User user = userService.getAuthenticatedUser();

        model.addAttribute("author", user.getUsername());
        model.addAttribute("action", "create");

        if (errors.hasErrors()) {
            return "Post-New-View";
        }

        Post post = postMapper.createPostFromDto(createPostDTO, user);
        postService.create(post, user);
        return format(REDIRECT, post.getId());

    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        User user = userService.getAuthenticatedUser();
        postService.delete(id, user);
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
