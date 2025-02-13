package com.example.forumproject.controllers.mvc;

import com.example.forumproject.exceptions.AuthenticationFailureException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.homepageResponseDtos.LoginDto;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mvc")
public class MvcHomepageController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;

    public MvcHomepageController(AuthenticationHelper authenticationHelper, UserService userService) {
        this.authenticationHelper = authenticationHelper;
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

    @GetMapping("/auth/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginDto());
        return "Login-View";
    }

    @PostMapping(("/auth/login"))
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
            return "redirect:/mvc/home";
        } catch (EntityNotFoundException e) {
            errors.rejectValue("username", "username.mismatch", "invalid username or password");
            return "Login-View";
        }
    }

    @GetMapping("/admin")
    public String showAdminPortalView(HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (!user.isAdmin()) {
                model.addAttribute("status", HttpStatus.NOT_FOUND.value()
                        + " " + HttpStatus.NOT_FOUND.getReasonPhrase());
                model.addAttribute("error", "You are not allowed to view this page!");
                return "Not-Found-View";
            }
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        return "Admin-View";
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}