package com.example.forumproject.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PublicMvc {

    @GetMapping
    public String redirectToHomepage() {
        return "redirect:/mvc/home";
    }

    @GetMapping("/mvc/home")
    public String showHomepage() {
        return "Home-View";
    }

    @GetMapping("/mvc/about")
    public String showAboutPage() {
        return "About-View";
    }
}