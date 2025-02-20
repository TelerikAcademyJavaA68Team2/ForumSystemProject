package com.example.forumproject.controllers.mvc;

import com.example.forumproject.mappers.UserMapper;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.adminResponceDtos.FullProfileAdminDto;
import com.example.forumproject.models.dtos.adminResponceDtos.FullProfileUserDto;
import com.example.forumproject.models.dtos.userDtos.DeleteAccountRequest;
import com.example.forumproject.models.dtos.userDtos.RequestUserProfileDto;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mvc/profile")
public class ProfileMvc {

    private final UserMapper userMapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileMvc(UserMapper userMapper, UserService userService, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getProfile(Model model) {
        User user = userService.getAuthenticatedUser();
        Object Dto = userMapper.mapUserToUserFullProfileOutDto(user);
        if (user.isAdmin()) {
            FullProfileAdminDto profileDto = (FullProfileAdminDto) Dto;
            model.addAttribute("user", profileDto);
        } else {
            FullProfileUserDto profileDto = (FullProfileUserDto) Dto;
            model.addAttribute("user", profileDto);
        }

        return "Profile-View";
    }


    @GetMapping("/update")
    public String showEditProfileForm(Model model) {
        User user = userService.getAuthenticatedUser();

        RequestUserProfileDto userProfileDto = new RequestUserProfileDto();
        userProfileDto.setFirstName(user.getFirstName());
        userProfileDto.setLastName(user.getLastName());
        userProfileDto.setEmail(user.getEmail());
        if (user.isAdmin()) {
            userProfileDto.setPhoneNumber(user.getPhoneNumber() == null ? "Please provide Phone Number!" : user.getPhoneNumber());
        }

        model.addAttribute("updateRequest", userProfileDto);
        model.addAttribute("isAdmin", user.isAdmin());
        return "Edit-Profile";
    }

    @PostMapping("/update")
    public String updateProfile(
            @Valid @ModelAttribute("updateRequest") RequestUserProfileDto updateProfileRequest, BindingResult errors) {
        if (errors.hasErrors()) {
            return "Edit-Profile";
        }
        User user = userService.getAuthenticatedUser();
        if (!passwordEncoder.matches(updateProfileRequest.getCurrentPassword(), user.getPassword())) {
            errors.rejectValue("currentPassword", "password.mismatch", "Wrong password");
            return "Edit-Profile";
        }

        if (!user.getEmail().equals(updateProfileRequest.getEmail())) {
            try {
                userService.getByEmail(updateProfileRequest.getEmail());
                errors.rejectValue("email", "email.mismatch", "Email is already in use");
                return "Edit-Profile";
            } catch (Exception ignored) {
            }
        }

        if ((!updateProfileRequest.getNewPassword().isBlank() || !updateProfileRequest.getNewPasswordRepeat().isBlank()) &&
                !updateProfileRequest.getNewPassword().equals(updateProfileRequest.getNewPasswordRepeat())) {
            errors.rejectValue("newPassword", "password.mismatch", "Password repeat failed");
            return "Edit-Profile";
        }
        if (!updateProfileRequest.getNewPassword().isBlank()) {
            if (updateProfileRequest.getNewPassword().length() < 2) {
                errors.rejectValue("newPassword", "password.mismatch", "Password repeat failed");
                return "Edit-Profile";
            }
            user.setPassword(passwordEncoder.encode(updateProfileRequest.getNewPassword()));
        }
        user.setFirstName(updateProfileRequest.getFirstName());
        user.setLastName(updateProfileRequest.getLastName());
        user.setEmail(updateProfileRequest.getEmail());
        if (!updateProfileRequest.getProfilePhoto().isEmpty()) {
            user.setPhoto(updateProfileRequest.getProfilePhoto());
        }
        if (user.isAdmin() && updateProfileRequest.getPhoneNumber() != null && !updateProfileRequest.getPhoneNumber().equals("No phone number provided")) {
            user.setPhoneNumber(updateProfileRequest.getPhoneNumber());
        }
        userService.update(user);
        return "redirect:/mvc/profile";
    }

    @GetMapping("/delete")
    public String getDeleteAccountPage(Model model) {
        model.addAttribute("request", new DeleteAccountRequest());
        return "Delete-Account";
    }

    @PostMapping("/delete")
    public String executeDeleteAccountRequest(@Valid @ModelAttribute("request") DeleteAccountRequest request, BindingResult errors, HttpSession session) {
        if (errors.hasErrors()) {
            return "Delete-Account";
        }
        if (!request.getCapcha().equalsIgnoreCase("Delete my account")) {
            errors.rejectValue("capcha", "capcha.mismatch", "Wrong Capcha");
            return "Delete-Account";
        }
        if (!passwordEncoder.matches(request.getPassword(), userService.getAuthenticatedUser().getPassword())) {
            errors.rejectValue("password", "password.mismatch", "Wrong Password");
            return "Delete-Account";
        }
        userService.deleteUser();
        SecurityContextHolder.clearContext();
        session.invalidate();
        return "redirect:/mvc/home";
    }
}
