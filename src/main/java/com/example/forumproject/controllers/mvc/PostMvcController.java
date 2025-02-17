package com.example.forumproject.controllers.mvc;

import com.example.forumproject.mappers.PostMapper;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;
import com.example.forumproject.models.dtos.postDtos.PostFilterDto;
import com.example.forumproject.models.dtos.postDtos.PostOutDto;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.tagService.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mvc/posts")
public class PostMvcController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final TagService tagService;

    public PostMvcController(PostService postService, PostMapper postMapper, TagService tagService) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.tagService = tagService;
    }

    @ModelAttribute("tags")
    public List<Tag> populateTags() {
        return tagService.getAllTags();
    }

    @GetMapping
    public String showAllPosts(@ModelAttribute("filterOptions") PostFilterDto postFilterDto, Model model){
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
}
