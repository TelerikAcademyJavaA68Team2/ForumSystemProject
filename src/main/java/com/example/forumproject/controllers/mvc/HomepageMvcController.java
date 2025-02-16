package com.example.forumproject.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mvc")
public class HomepageMvcController {

    @GetMapping("/home")
    public String showHomepage() {
        return "Home-View";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "About-View";
    }
}