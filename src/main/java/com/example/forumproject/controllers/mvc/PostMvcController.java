package com.example.forumproject.controllers.mvc;

import com.example.forumproject.mappers.PostMapper;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.dtos.postDtos.PostFilterDto;
import com.example.forumproject.models.dtos.postDtos.PostOutDto;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
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
    private final UserService userService;
    private final PostMapper postMapper;

    public PostMvcController(PostService postService, UserService userService, PostMapper postMapper) {
        this.postService = postService;
        this.userService = userService;
        this.postMapper = postMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showAllPosts(@ModelAttribute("filterOptions") PostFilterDto postFilterDto, Model model, HttpSession session){
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

        if (populateIsAuthenticated(session)){
            String currentUsername = (String) session.getAttribute("currentUser");
            model.addAttribute("currentUser", userService.loadUserByUsername(currentUsername));
        }

        model.addAttribute("filterOptions", postFilterDto);
        model.addAttribute("posts", posts);

        return "Posts-View";
    }

}
