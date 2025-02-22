package com.example.forumproject.controllers.mvc;

import com.example.forumproject.exceptions.InvalidUserInputException;
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

import java.io.IOException;

import static com.example.forumproject.helpers.ValidationHelpers.isValidImageFile;

@Controller
@RequestMapping("/mvc/profile")
public class ProfileMvcController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryHelper cloudinaryHelper;

    final long MAX_FILE_SIZE = 5 * 1024 * 1024;

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
            @RequestParam(value = "removeProfile", required = false, defaultValue = "false") boolean removeProfile,
            BindingResult errors) {

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
        if (!updateProfileRequest.getNewPassword().isBlank() && updateProfileRequest.getNewPassword().length() >= 2) {
            user.setPassword(passwordEncoder.encode(updateProfileRequest.getNewPassword()));
        }

        user.setFirstName(updateProfileRequest.getFirstName());
        user.setLastName(updateProfileRequest.getLastName());
        user.setEmail(updateProfileRequest.getEmail());
        if (removeProfile) {
            user.setPhoto("/images/default-profile-pic.png");
        } else if (profileImage != null && !profileImage.isEmpty()) {
            try {
                if (!isValidImageFile(profileImage)) {
                    errors.rejectValue("profilePhoto", "profilePhoto.invalid", "Invalid image file. Only JPG, PNG, or GIF files are allowed.");
                    return "Edit-Profile";
                }
                if (profileImage.getSize() > MAX_FILE_SIZE) {

                    errors.rejectValue("profilePhoto", "profilePhoto.tooLarge", "File is too large! Max size is 5MB.");
                    return "Edit-Profile";
                }
                String imageUrl = cloudinaryHelper.uploadUserProfilePhoto(profileImage, user);
                user.setPhoto(imageUrl);
            } catch (IOException | InvalidUserInputException e) {
                errors.rejectValue("profilePhoto", "profilePhoto.mismatch", "Invalid image file. Only JPEG, PNG, or GIF files are allowed.");
                return "Edit-Profile";
            }
        }

        if (user.isAdmin() && updateProfileRequest.getPhoneNumber() != null &&
                !updateProfileRequest.getPhoneNumber().equals("No phone number provided")) {
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
        if (!request.getCaptcha().equalsIgnoreCase("Delete my account")) {
            errors.rejectValue("captcha", "captcha.mismatch", "Wrong Captcha");
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
