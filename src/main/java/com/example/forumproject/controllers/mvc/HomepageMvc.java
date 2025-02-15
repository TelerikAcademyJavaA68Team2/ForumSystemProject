package com.example.forumproject.controllers.mvc;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.homepageResponseDtos.LoginDto;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mvc")
public class HomepageMvc {

    private final UserService userService;

    public HomepageMvc(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String showHomepage() {
        return "Home-View";
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "About-View";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginDto());
        return "Login-View";
    }

    @PostMapping(("/login"))
    public String executeLoginRequest(@Valid @ModelAttribute("loginRequest") LoginDto loginRequest, BindingResult errors, HttpSession session) {
        if (errors.hasErrors()) {
            return "Login-View";
        }
        try {
            User user = userService.loadUserByUsername(loginRequest.getUsername());
            if (!user.getPassword().equals(loginRequest.getPassword())) {
                throw new EntityNotFoundException("");
            }
            session.setAttribute("currentUser", loginRequest.getUsername());
            session.setAttribute("hasActiveUser", true);
            session.setAttribute("isUserAdmin", user.isAdmin());
            return "redirect:/mvc/home";
        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            errors.rejectValue("username", "username.mismatch", "invalid username or password");
            return "Login-View";
        }
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}