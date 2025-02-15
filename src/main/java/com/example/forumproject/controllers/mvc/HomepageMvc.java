package com.example.forumproject.controllers.mvc;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.InvalidUserInputException;
import com.example.forumproject.models.dtos.homepageResponseDtos.LoginDto;
import com.example.forumproject.models.dtos.homepageResponseDtos.UserRegistrationDto;
import com.example.forumproject.services.securityServices.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final AuthenticationService authenticationService;

    @Autowired
    public HomepageMvc(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
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
            authenticationService.authenticateForMvc(loginRequest, session);
            return "redirect:/mvc/home";
        } catch (BadCredentialsException e) {
            errors.rejectValue("username", "username.mismatch", "Invalid username or password");
            return "Login-View";
        } catch (DisabledException e) {
            return "redirect:/mvc/blocked";
        } catch (Exception e) {
            errors.rejectValue("username", "username.error", "An error occurred during login");
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
            authenticationService.registerForMvc(registerRequest);
            return "redirect:/mvc/login";
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