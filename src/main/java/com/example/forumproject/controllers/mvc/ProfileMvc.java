package com.example.forumproject.controllers.mvc;

import com.example.forumproject.mappers.UserMapper;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.adminResponceDtos.FullProfileAdminDto;
import com.example.forumproject.models.dtos.adminResponceDtos.FullProfileUserDto;
import com.example.forumproject.models.dtos.userDtos.RequestUserProfileDto;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.UserService;
import jakarta.validation.Valid;
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
    private final PostService postService;


    public ProfileMvc(UserMapper userMapper, UserService userService, PostService postService) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.postService = postService;
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
            @Valid @ModelAttribute("updateRequest") RequestUserProfileDto userProfileDto, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            return "Edit-Profile";
        }
        User user = userService.getAuthenticatedUser();
        if (!user.getEmail().equals(userProfileDto.getEmail())) {
            try {
                userService.getByEmail(userProfileDto.getEmail());
                errors.rejectValue("email", "email.mismatch", "Email is already in use");
                return "Edit-Profile";
            } catch (Exception ignored) {
            }
        }
        user.setFirstName(userProfileDto.getFirstName());
        user.setLastName(userProfileDto.getLastName());
        user.setEmail(userProfileDto.getEmail());
        if (!userProfileDto.getProfilePhoto().isEmpty()) {
            user.setPhoto(userProfileDto.getProfilePhoto());
        }
        if (user.isAdmin() && userProfileDto.getPhoneNumber() != null && !userProfileDto.getPhoneNumber().equals("No phone number provided")) {
            user.setPhoneNumber(userProfileDto.getPhoneNumber());
        }
        userService.update(user);
        return "redirect:/mvc/profile";
    }

}
