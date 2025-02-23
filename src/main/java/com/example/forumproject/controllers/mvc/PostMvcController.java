package com.example.forumproject.controllers.mvc;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.mappers.CommentMapper;
import com.example.forumproject.mappers.PostMapper;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.commentDtos.CommentOutDto;
import com.example.forumproject.models.dtos.postDtos.PostFilterDto;
import com.example.forumproject.models.dtos.postDtos.PostInDto;
import com.example.forumproject.models.dtos.postDtos.PostOutDto;
import com.example.forumproject.models.dtos.postDtos.PostUpdateDto;
import com.example.forumproject.models.dtos.tagDtos.TagInDto;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.UserService;
import com.example.forumproject.services.commentService.CommentService;
import com.example.forumproject.services.postTagService.PostTagService;
import com.example.forumproject.services.reactionService.ReactionService;
import com.example.forumproject.services.tagService.TagService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final PostTagService postTagService;


    public PostMvcController(PostService postService, PostMapper postMapper, TagService tagService, UserService userService, ReactionService reactionService, CommentMapper commentMapper, CommentService commentService, PostTagService postTagService) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.tagService = tagService;
        this.userService = userService;
        this.reactionService = reactionService;
        this.commentMapper = commentMapper;
        this.commentService = commentService;
        this.postTagService = postTagService;
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

    @GetMapping("/new")
    public String showCreatePostPage(Model model) {
        User user = userService.getAuthenticatedUser();

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
    public String deletePost(@PathVariable Long id,
                             @RequestHeader(value = "referer", required = false) String referer) {
        User user = userService.getAuthenticatedUser();
        postService.delete(id, user);

        if (referer != null && referer.contains("/profile")) {
            return "redirect:/mvc/profile";
        }
        return "redirect:/mvc/posts";
    }

    @PostMapping("/{id}/comments/new")
    public String addComment(@PathVariable Long id,
                             @Valid @ModelAttribute("comment") CommentOutDto commentDto,
                             BindingResult errors,
                             Model model) {

        User user = userService.getAuthenticatedUser();
        model.addAttribute("author", user.getUsername());

        if (errors.hasErrors()) {
            return "Post-View";
        }

        Comment newComment = commentMapper.commentOutDtoToObject(commentDto);
        commentService.create(id, newComment);

        return format(REDIRECT, id);
    }

    @PostMapping("/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {

        commentService.delete(postId, commentId);

        return "redirect:/mvc/posts/" + postId;
    }

    @PostMapping("/{id}/comments/{commentId}/edit")
    public String editComment(@PathVariable Long id,
                              @PathVariable Long commentId,
                              @Valid @ModelAttribute("comment") CommentOutDto commentDto,
                              BindingResult errors,
                              Model model) {

        User user = userService.getAuthenticatedUser();
        model.addAttribute("author", user.getUsername());

        if (errors.hasErrors()) {
            return "Post-View";
        }

        Comment comment = commentService.getById(id, commentId);

        if (!comment.getAuthor().equals(user)) {
            return "redirect:/mvc/posts/" + id;
        }

        comment.setContent(commentDto.getContent());
        try {
            commentService.update(id, comment);
        } catch (DuplicateEntityException e) {
            return format(REDIRECT, id);
        }
        return "redirect:/mvc/posts/" + id;
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

    @GetMapping("/{id}/edit")
    public String showEditPostView(@PathVariable Long id, @RequestParam(required = false, defaultValue = "") List<String> tags, Model model) {

        Post post = postService.getById(id);
        User user = post.getAuthor();
        if (!user.isAdmin() && !post.getAuthor().equals(userService.getAuthenticatedUser())) {
            return format(REDIRECT, post.getId());
        }

        PostUpdateDto updatePostDTO = postMapper.postToPostUpdateDto(id);
        model.addAttribute("post", updatePostDTO);

        List<String> currentTags = tags.isEmpty() ? postTagService.getTagsByPostId(id).stream().map(Tag::getTagName).toList() : tags.stream().filter(e-> !Objects.equals(e, "0")).toList();
        List<String> currentTags2 = new ArrayList<>(currentTags);
        model.addAttribute("currentTags", currentTags);
        model.addAttribute("currentTags2", currentTags2);
        model.addAttribute("newTagDto", new TagInDto(currentTags2));
        StringBuilder queryString = new StringBuilder();
        for (String name : currentTags2) {
            queryString.append(name).append("&tags=");
        }
        if (queryString.length() >= 6) {

            queryString = new StringBuilder(queryString.substring(0, queryString.length() - 6));
        }


        model.addAttribute("query", (queryString.toString()));

        return "Edit-Post-View";
    }

    @PostMapping("/{id}/edit")
    public String editPost(@PathVariable Long id, @RequestParam(required = false, defaultValue = "") List<String> tags,
                           @Valid @ModelAttribute("post") PostUpdateDto updatePostDTO, BindingResult errors) {
        if (errors.hasErrors()) {
            return "Edit-Post-View";
        }

        try {
            Post originalPost = postService.getById(id);
            User user = userService.getAuthenticatedUser();
            if (!originalPost.getAuthor().equals(user) && !user.isAdmin()) {
                throw new UnauthorizedAccessException("You do not have permission to edit this post.");
            }

            List<Tag> currentTags = postTagService.getTagsByPostId(id);
            currentTags.stream().filter(e -> !tags.contains(e.getTagName())).forEach(e -> postTagService.deleteTagFromPost(id, e.getId()));

            List<Tag> newTags = tags.stream().map(tagService::getOrCreateAndGetTagByName).filter(e -> !currentTags.contains(e)).toList();
            for (Tag tag : newTags) {
                postTagService.createTagOnPost(id, tag.getTagName());
            }

            Post updatedPost = postMapper.dtoToObject(updatePostDTO);
            postService.update(updatedPost, user);

            return format(REDIRECT, id);
        } catch (IllegalArgumentException i) {
            errors.rejectValue("tags", "tags-invalid", i.getMessage());
            return "Edit-Post-View";
        } catch (UnauthorizedAccessException e) {
            return "Forbidden-View";
        }
    }

    @PostMapping("/{id}/edit/add-tag")
    public String addTagToPost(@PathVariable Long id,
                               @Valid @ModelAttribute("newTagDto") TagInDto newTagDto,
                               BindingResult errors) {
        if (errors.hasErrors()) {
            return String.format("redirect:/mvc/posts/%d/edit?tags=%s", id, String.join("&tags=", newTagDto.getTagsList()));
        }
        String newTagName = newTagDto.getTagName().toLowerCase();
        List<String> updatedTags;
        if (newTagDto.getTagsList().isEmpty()) {
            updatedTags = new ArrayList<>();
        } else {
            updatedTags = newTagDto.getTagsList();
        }
        if (!updatedTags.contains(newTagName)) {
            updatedTags.add(newTagName);
        }

        return String.format("redirect:/mvc/posts/%d/edit?tags=%s", id, String.join("&tags=", updatedTags));
    }

    @GetMapping("/{id}/edit/remove-tag/{tagName}")
    public String removeTagFromPost(@PathVariable Long id,
                                    @PathVariable String tagName,
                                    @RequestParam(required = false, defaultValue = "") List<String> tags) {

        List<String> updatedTags = tags.stream()
                .filter(existingTag -> !existingTag.equalsIgnoreCase(tagName))
                .toList();

        return String.format(
                "redirect:/mvc/posts/%d/edit%s", id, updatedTags.isEmpty() ? "?tags=0" : "?tags=" +
                        String.join("&tags=", updatedTags));
    }
}
