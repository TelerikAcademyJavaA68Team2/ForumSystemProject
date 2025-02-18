package com.example.forumproject.controllers.mvc;

import com.example.forumproject.exceptions.InvalidUserInputException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.mappers.UserMapper;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.adminResponceDtos.FullProfileAdminDto;
import com.example.forumproject.models.dtos.adminResponceDtos.FullProfileUserDto;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mvc/admin")
public class AdminMvcController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public AdminMvcController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String showAdminPortalView2(HttpSession session, Model model) {
//        String redirectUrl = redirectIfNecessary(session);
//        if (redirectUrl != null) return redirectUrl;

        User user = userService.getAuthenticatedUser();

        model.addAttribute("user", user);
        return "Admin-View";
    }

//    private static String redirectIfNecessary(HttpSession session) {
//        if (!(boolean) session.getAttribute("hasActiveUser")) {
//            return "redirect:/mvc/auth/login";
//        }
//        if (!(boolean) session.getAttribute("isUserAdmin")) {
//            return "Forbidden-View";
//        }
//        return null;
//    }


    @GetMapping("/users/{id}")
    public String getUserProfile(@PathVariable Long id, Model model) {
        User user = userService.getById(id);
        Object Dto = userMapper.mapUserToUserFullProfileOutDto(user);
        if (user.isAdmin()) {
            FullProfileAdminDto profileDto = (FullProfileAdminDto) Dto;
            model.addAttribute("user", profileDto);
        } else {
            FullProfileUserDto profileDto = (FullProfileUserDto) Dto;
            model.addAttribute("user", profileDto);
        }
        return "User-View";
    }

    @GetMapping("/users/{id}/make-admin")
    public String updateUserToAdmin(@PathVariable Long id) {
        try {
            userService.promoteToAdmin(id);
        } catch (InvalidUserInputException e) {
            return String.format("redirect:/mvc/admin/users/%d", id);
        }

        return String.format("redirect:/mvc/admin/users/%d", id);
    }

    @GetMapping("/users/{id}/make-user")
    public String demoteAdminToUser(@PathVariable Long id) {
        try {
            userService.demoteAdminToUser(id);
        } catch (InvalidUserInputException e) {
            return String.format("redirect:/mvc/admin/users/%d", id);
        }

        return String.format("redirect:/mvc/admin/users/%d", id);
    }

    @GetMapping("/users/{id}/block")
    public String blockUser(@PathVariable Long id) {
        try {
            userService.blockUser(id);
        } catch (InvalidUserInputException e) {
            return String.format("redirect:/mvc/admin/users/%d", id);
        }

        return String.format("redirect:/mvc/admin/users/%d", id);
    }

    @GetMapping("/users/{id}/unblock")
    public String unblockUser(@PathVariable Long id) {
        try {
            userService.unblockUser(id);
        } catch (InvalidUserInputException e) {
            return String.format("redirect:/mvc/admin/users/%d", id);
        }

        return String.format("redirect:/mvc/admin/users/%d", id);
    }


}