package com.example.forumproject.models.dtos.homepageResponseDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {

    @NotBlank(message = "First name can't be blank")
    @Size(min = 4, max = 32, message = "First name should be between 4 and 32 symbols!")
    private String firstName;

    @NotBlank(message = "Last name can't be blank")
    @Size(min = 4, max = 32, message = "Last name should be between 4 and 32 symbols!")
    private String lastName;

    @NotBlank(message = "Email can't be blank")
    @Size(min = 5, max = 200, message = "Email should be between 5 and 250 symbols!")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email address is invalid!")
    private String email;

    @NotBlank(message = "Username can't be blank")
    @Size(min = 2, max = 20, message = "username should be between 2 and 20 symbols!")
    private String username;

    @NotBlank(message = "Password can't be blank")
    @Size(min = 2, max = 20, message = "password should be between 2 and 20 symbols!")
    private String password;

    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String firstName, String lastName, String email, String username, String password) {
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
