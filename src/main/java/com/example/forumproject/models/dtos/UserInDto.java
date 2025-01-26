package com.example.forumproject.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserInDto {

    @NotNull(message = "First name can't be empty")
    @Size(min = 2, max = 20, message = "First name should be between 2 and 20 symbols!")
    private String firstName;

    @NotNull(message = "Last name can't be empty")
    @Size(min = 2, max = 20, message = "Last name should be between 2 and 20 symbols!")
    private String lastName;

    @NotNull(message = "Email can't be empty")
    @Size(min = 2, max = 20, message = "Email should be between 2 and 20 symbols!")
    private String email;

    @NotNull(message = "Username can't be empty")
    @Size(min = 2, max = 20, message = "username should be between 2 and 20 symbols!")
    private String username;

    @NotNull(message = "Password can't be empty")
    @Size(min = 2, max = 20, message = "password should be between 2 and 20 symbols!")
    private String password;

    public UserInDto() {
    }

    public UserInDto(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
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
}
