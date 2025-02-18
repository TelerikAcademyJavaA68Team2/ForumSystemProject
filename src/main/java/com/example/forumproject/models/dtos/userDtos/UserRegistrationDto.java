package com.example.forumproject.models.dtos.userDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {

    @NotBlank(message = "First name should be between 4 and 32 symbols!")
    @Size(min = 4, max = 32, message = "First name should be between 4 and 32 symbols!")
    private String firstName;

    @NotBlank(message = "Last name should be between 4 and 32 symbols!")
    @Size(min = 4, max = 32, message = "Last name should be between 4 and 32 symbols!")
    private String lastName;

    @NotBlank(message = "Email address is invalid!")
    @Email(message = "Email address is invalid!")
    @Size(min = 5, max = 200, message = "Email should be between 5 and 250 symbols!")
    private String email;

    @NotBlank(message = "Username should be between 2 and 20 symbols!")
    @Size(min = 2, max = 20, message = "Username should be between 2 and 20 symbols!")
    private String username;

    @NotBlank(message = "Password should be between 2 and 20 symbols!")
    @Size(min = 2, max = 20, message = "Password should be between 2 and 20 symbols!")
    private String password;

    @NotBlank(message = "Password should be between 2 and 20 symbols!")
    @Size(min = 2, max = 20, message = "Password should be between 2 and 20 symbols!")
    private String passwordConfirm;

    public UserRegistrationDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}