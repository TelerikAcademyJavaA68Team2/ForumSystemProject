package com.example.forumproject.controllers.mvc;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.InvalidUserInputException;
import com.example.forumproject.models.dtos.userDtos.LoginDto;
import com.example.forumproject.models.dtos.userDtos.UserRegistrationDto;
import com.example.forumproject.services.securityServices.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mvc/auth")
public class AuthenticationMvcController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationMvcController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, HttpSession session,
                               @RequestParam(value = "error", required = false) Boolean error,
                               @RequestParam(value = "blocked", required = false) Boolean blocked) {
        if (Boolean.TRUE.equals(error)) {
            model.addAttribute("errorMessage", "Invalid username or password");
        } else if (Boolean.TRUE.equals(blocked)) {
            model.addAttribute("errorMessage", "Your account is blocked");
        }

        Boolean hasActiveUser = (Boolean) session.getAttribute("hasActiveUser");
        if (hasActiveUser != null && hasActiveUser) {
            return "redirect:/mvc/home";
        }

        model.addAttribute("loginDto", new LoginDto());
        return "Login-View";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model, HttpSession session) {
        Boolean hasActiveUser = (Boolean) session.getAttribute("hasActiveUser");
        if (hasActiveUser != null && hasActiveUser) {
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
            authenticationService.registerForMvc(registerRequest);
            return "redirect:/mvc/auth/login";
        } catch (DuplicateEntityException e) {
            String field = "email";
            String errorCode = "email.mismatch";
            String defaultMsg = "Email already taken";

            if (e.getMessage().startsWith("User with username")) {
                field = "username";
                errorCode = "username.mismatch";
                defaultMsg = "Username already taken";
            }
            errors.rejectValue(field, errorCode, defaultMsg);
            return "Register-View";
        } catch (InvalidUserInputException e) {
            errors.rejectValue("passwordConfirm", "password.mismatch", e.getMessage());
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