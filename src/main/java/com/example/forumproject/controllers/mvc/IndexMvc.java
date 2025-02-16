package com.example.forumproject.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexMvc {

    @GetMapping
    public String redirectToHomepage() {
        return "redirect:/mvc/home";
    }
}
