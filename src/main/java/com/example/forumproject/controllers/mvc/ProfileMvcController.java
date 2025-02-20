package com.example.forumproject.controllers.mvc;

import com.example.forumproject.helpers.CloudinaryHelper;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/mvc/profile")
public class ProfileMvcController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryHelper cloudinaryHelper;

    @Autowired
    public ProfileMvcController(UserMapper userMapper, UserService userService, PasswordEncoder passwordEncoder, CloudinaryHelper cloudinaryHelper) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryHelper = cloudinaryHelper;
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
        userProfileDto.setProfilePhoto(user.getPhoto());
        if (user.isAdmin()) {
            userProfileDto.setPhoneNumber(user.getPhoneNumber() == null ? "Please provide Phone Number!" : user.getPhoneNumber());
        }

        model.addAttribute("updateRequest", userProfileDto);
        model.addAttribute("isAdmin", user.isAdmin());
        model.addAttribute("profilePhoto", user.getPhoto());
        return "Edit-Profile";
    }

    @PostMapping("/update")
    public String updateProfile(
            @Valid @ModelAttribute("updateRequest") RequestUserProfileDto updateProfileRequest,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            BindingResult errors, RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return "Edit-Profile";
        }

        User user = userService.getAuthenticatedUser();

        // Verify the current password
        if (!passwordEncoder.matches(updateProfileRequest.getCurrentPassword(), user.getPassword())) {
            errors.rejectValue("currentPassword", "password.mismatch", "Wrong password");
            return "Edit-Profile";
        }

        // Check if email is changing and validate it
        if (!user.getEmail().equals(updateProfileRequest.getEmail())) {
            try {
                userService.getByEmail(updateProfileRequest.getEmail());
                errors.rejectValue("email", "email.mismatch", "Email is already in use");
                return "Edit-Profile";
            } catch (Exception ignored) {
            }
        }

        // Handle password update
        if ((!updateProfileRequest.getNewPassword().isBlank() || !updateProfileRequest.getNewPasswordRepeat().isBlank()) &&
                !updateProfileRequest.getNewPassword().equals(updateProfileRequest.getNewPasswordRepeat())) {
            errors.rejectValue("newPassword", "password.mismatch", "Password repeat failed");
            return "Edit-Profile";
        }
        if (!updateProfileRequest.getNewPassword().isBlank() && updateProfileRequest.getNewPassword().length() >= 2) {
            user.setPassword(passwordEncoder.encode(updateProfileRequest.getNewPassword()));
        }

        // Update basic user details
        user.setFirstName(updateProfileRequest.getFirstName());
        user.setLastName(updateProfileRequest.getLastName());
        user.setEmail(updateProfileRequest.getEmail());

        // Upload and update profile picture if provided
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String imageUrl = cloudinaryHelper.uploadUserProfilePhoto(profileImage, user);
                user.setPhoto(imageUrl);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error uploading profile picture.");
            }
        }

        // Update phone number (for admins)
        if (user.isAdmin() && updateProfileRequest.getPhoneNumber() != null &&
                !updateProfileRequest.getPhoneNumber().equals("No phone number provided")) {
            user.setPhoneNumber(updateProfileRequest.getPhoneNumber());
        }

        userService.update(user);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/mvc/profile";
    }


    @PostMapping("/upload-photo")
    public String uploadProfilePhoto(@RequestParam("image") MultipartFile image, RedirectAttributes redirectAttributes) {
        try {

            User user = userService.getAuthenticatedUser();

            cloudinaryHelper.uploadUserProfilePhoto(image, user);


            redirectAttributes.addFlashAttribute("successMessage", "Profile photo updated successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading image. Please try again.");
        }

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
