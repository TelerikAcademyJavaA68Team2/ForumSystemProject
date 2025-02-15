package com.example.forumproject.services.securityServices;

import com.example.forumproject.exceptions.InvalidUserInputException;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.homepageResponseDtos.LoginDto;
import com.example.forumproject.models.dtos.homepageResponseDtos.UserRegistrationDto;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    public static final String WRONG_CREDENTIALS = "Wrong username or password!";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServiceImpl(UserService userService,
                                     PasswordEncoder passwordEncoder,
                                     JwtService jwtService,
                                     AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void authenticateForMvc(LoginDto request, HttpSession session) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        session.setAttribute("currentUser", request.getUsername());
        session.setAttribute("hasActiveUser", true);
        session.setAttribute("isUserAdmin", user.isAdmin());
    }

    @Override
    public void registerForMvc(UserRegistrationDto request) {
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new InvalidUserInputException("invalid password confirmation");
        }
        User user = createUserFromRequest(request);
        userService.save(user);
    }

    public String register(UserRegistrationDto request) {
        if (!request.getPasswordConfirm().equals(request.getPassword())) {
            throw new InvalidUserInputException("Password Confirmation failed");
        }
        User user = createUserFromRequest(request);
        userService.save(user);
        return jwtService.generateToken(user);
    }

    public String authenticate(LoginDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, WRONG_CREDENTIALS);
        }

        User user = userService.loadUserByUsername(request.getUsername());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return jwtService.generateToken(user);
    }

    private User createUserFromRequest(UserRegistrationDto request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return user;
    }
}