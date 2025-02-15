package com.example.forumproject.controllers.mvc;

import com.example.forumproject.models.User;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mvc")
public class AdminMvc {

    private final UserService userService;

    @Autowired
    public AdminMvc(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String showAdminPortalView2(HttpSession session, Model model) {
        String redirectUrl = redirectIfNecessary(session);
        if (redirectUrl != null) return redirectUrl;

        User user = userService.loadUserByUsername((String) session.getAttribute("currentUser"));

        model.addAttribute("user", user);
        return "Admin-View";
    }

    private static String redirectIfNecessary(HttpSession session) {
        if (!(boolean) session.getAttribute("hasActiveUser")) {
            return "redirect:/mvc/login";
        }
        if (!(boolean) session.getAttribute("isUserAdmin")) {
            return "Forbidden-View";
        }
        return null;
    }
}