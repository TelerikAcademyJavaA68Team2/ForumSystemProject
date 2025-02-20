package com.example.forumproject.controllers.mvc;

import com.example.forumproject.mappers.PostMapper;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.dtos.postDtos.PostOutDto;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class PublicMvcController {

    private final UserService userService;
    private final PostService postService;
    private final PostMapper postMapper;

    public PublicMvcController(UserService userService, PostService postService, PostMapper postMapper) {
        this.userService = userService;
        this.postService = postService;
        this.postMapper = postMapper;
    }

    @GetMapping
    public String redirectToHomepage() {
        return "redirect:/mvc/home";
    }

    @GetMapping("/mvc/home")
    public String homePage(@RequestParam(required = false, defaultValue = "viral") String listType, Model model) {
        long totalUsers = userService.getNumberOfRegisteredUsers();
        long totalPosts = postService.getTotalNumberOfPosts();

        List<PostOutDto> postsDto;
        if ("viral".equalsIgnoreCase(listType)) {
            List<Post> posts = postService.getAllPosts(new PostFilterOptions(null, null, null, null, null, "comments", "desc", null));
            postsDto = posts.stream().map(postMapper::postToPostOutDto).limit(10).toList();
            model.addAttribute("posts", postsDto);
            model.addAttribute("currentListType", "viral");
        } else {
            List<Post> posts = postService.getAllPosts(new PostFilterOptions(null, null, null, null, null, "id", "desc", null));
            postsDto = posts.stream().map(postMapper::postToPostOutDto).limit(10).toList();
            model.addAttribute("posts", postsDto);
            model.addAttribute("currentListType", "recent");
        }

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalPosts", totalPosts);
        return "Home-View";
    }


    @GetMapping("/mvc/about")
    public String showAboutPage() {
        return "About-View";
    }

    @GetMapping("/mvc/error")
    public String showErrorPage() {
        return "Not-Found-View";
    }
}