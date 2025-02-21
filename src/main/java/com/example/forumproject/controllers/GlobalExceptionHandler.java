package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

//Implemented for MVC for easy exception handling
@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle 404 errors (No mapping found)
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        model.addAttribute("status", HttpStatus.NOT_FOUND);
        return "Not-Found-View";
    }

    // Handle 404 errors (No mapping found)
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleNotFound(EntityNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("status", HttpStatus.NOT_FOUND);
        return "Not-Found-View";
    }

    // Handle 405 errors (Wrong method used)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, Model model) {
        model.addAttribute("error", "This action is not allowed!");
        model.addAttribute("status", HttpStatus.METHOD_NOT_ALLOWED);
        return "Method-Not-Allowed";
    }

   // Handle 401 errors (Unauthorized)
    @ExceptionHandler(UnauthorizedAccessException.class)
    public String handleUnauthorizedAccessException(UnauthorizedAccessException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        redirectAttributes.addFlashAttribute("status", HttpStatus.UNAUTHORIZED);
        return "redirect:/mvc/auth/login";
    }

    // Handle 403 errors (Access Denied by Spring Security)
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "You must be logged in to perform this action!");
        redirectAttributes.addFlashAttribute("status", HttpStatus.FORBIDDEN);
        return "redirect:/mvc/auth/login";
    }

    // Handle 403 errors (Access Denied by Spring Security)
    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFoundException(NoResourceFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "This action is not allowed!");
        redirectAttributes.addFlashAttribute("status", HttpStatus.FORBIDDEN);
        return "Forbidden-View";
    }

// uncomment when done with project!

//    // Handle all other exceptions
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String handleGlobalException(Exception ex, Model model) {
//        model.addAttribute("param.error", "Something went wrong. Please try again later.");
//        return "Internal-Server-Error";
//    }
}
