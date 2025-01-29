package com.example.forumproject.models.dtos.userDtos;

import jakarta.validation.constraints.Email;

public class RequestUserProfileDto {
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String profilePhoto;
    private String phoneNumber;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}