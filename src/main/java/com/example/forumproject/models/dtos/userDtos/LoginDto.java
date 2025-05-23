package com.example.forumproject.models.dtos.userDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDto {

    @NotBlank(message = "Username can't be empty")
    @Size(min = 2, max = 20, message = "Username should be between 2 and 20 symbols!")
    private String username;

    @NotBlank(message = "Password can't be empty")
    @Size(min = 2, max = 20, message = "Password should be between 2 and 20 symbols!")
    private String password;

    public LoginDto() {
    }

    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}