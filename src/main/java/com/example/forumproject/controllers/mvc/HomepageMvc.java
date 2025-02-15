package com.example.forumproject.controllers.mvc;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.homepageResponseDtos.LoginDto;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public HomepageMvc(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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

    @PostMapping("/login")
    public String executeLoginRequest(@Valid @ModelAttribute("loginRequest") LoginDto loginRequest, BindingResult errors, HttpSession session) {
        if (errors.hasErrors()) {
            return "Login-View";
        }
        try {
            User user = userService.loadUserByUsername(loginRequest.getUsername());
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new EntityNotFoundException("");
            }
           /* if (user.isBlocked()){ redundant for spring security but test anyway
                return "redirect:/mvc/blocked";
            }*/
            session.setAttribute("currentUser", loginRequest.getUsername());
            session.setAttribute("hasActiveUser", true);
            session.setAttribute("isUserAdmin", user.isAdmin());
            return "redirect:/mvc/home";
        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            errors.rejectValue("username", "username.mismatch", "invalid username or password");
            return "Login-View";
        }
    }

    /*@ModelAttribute("isAuthenticated") redundant we have boolean "hasActiveUser" in the session
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }*/
}