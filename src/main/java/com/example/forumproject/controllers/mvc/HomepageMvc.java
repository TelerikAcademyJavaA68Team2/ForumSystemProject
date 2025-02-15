package com.example.forumproject.controllers.mvc;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.homepageResponseDtos.LoginDto;
import com.example.forumproject.models.dtos.homepageResponseDtos.UserRegistrationDto;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String getLoginPage(Model model, HttpSession session) {
        if ((boolean) session.getAttribute("hasActiveUser")) {
            return "redirect:/mvc/home";
        }
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

            session.setAttribute("currentUser", loginRequest.getUsername());
            session.setAttribute("hasActiveUser", true);
            session.setAttribute("isUserAdmin", user.isAdmin());
            return "redirect:/mvc/home";
        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            errors.rejectValue("username", "username.mismatch", "invalid username or password");
            return "Login-View";
        }
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model, HttpSession session) {
        if ((boolean) session.getAttribute("hasActiveUser")) {
            return "redirect:/mvc/home";
        }
        model.addAttribute("registerRequest", new UserRegistrationDto());
        return "Register-View";
    }

    @PostMapping(("/register"))
    public String executeRegisterRequest(@Valid @ModelAttribute("registerRequest") UserRegistrationDto registerRequest, BindingResult errors) {
        if (errors.hasErrors()) {
            return "Register-View";
        }
        try {
            if (!registerRequest.getPassword().equals(registerRequest.getPasswordConfirm())) {
                errors.rejectValue("passwordConfirm", "password.mismatch", "invalid password confirmation");
                return "Register-View";
            }
            User user = new User();
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setEmail(registerRequest.getEmail());
            user.setUsername(registerRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            userService.save(user);

            return "redirect:/mvc/login";
        } catch (DuplicateEntityException e) {
            String field;
            String errorCode;
            String defaultMsg;
            if (e.getMessage().startsWith("User with email")) {
                field = "email";
                errorCode = "email.mismatch";
                defaultMsg = "Email already taken";
            } else {
                field = "username";
                errorCode = "username.mismatch";
                defaultMsg = "Username already taken";
            }
            errors.rejectValue(field, errorCode, defaultMsg);
            return "Register-View";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();

        return "redirect:/mvc/home";
    }
}