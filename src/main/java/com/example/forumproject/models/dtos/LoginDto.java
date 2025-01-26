package com.example.forumproject.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginDto {

    @NotNull(message = "Username or email can't be empty")
    private String usernameOrEmail;

    @JsonIgnore
    @NotNull(message = "Password can't be empty")
    @Size(min = 2, max = 20, message = "Password should be between 2 and 20 symbols!")
    private String password;

    public LoginDto(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
