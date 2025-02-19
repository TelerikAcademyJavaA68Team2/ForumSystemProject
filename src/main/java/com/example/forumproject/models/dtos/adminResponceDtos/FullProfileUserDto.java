package com.example.forumproject.models.dtos.adminResponceDtos;

import com.example.forumproject.models.dtos.postDtos.PostOutDto;

import java.util.List;

public class FullProfileUserDto {

    private Long id;
    private String first_name;
    private String last_name;
    private String username;
    private String email;
    private String account_type;
    private String account_status;
    private String profile_photo;

    private List<PostOutDto> user_posts;

    public FullProfileUserDto(Long id, String first_name, String last_name, String username, String email, String account_type, String account_status, String profile_photo) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.username = username;
        this.account_type = account_type;
        this.account_status = account_status;
        this.profile_photo = profile_photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getAccount_status() {
        return account_status;
    }

    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public List<PostOutDto> getUser_posts() {
        return user_posts;
    }

    public void setUser_posts(List<PostOutDto> user_posts) {
        this.user_posts = user_posts;
    }
}