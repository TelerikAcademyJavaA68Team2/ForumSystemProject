package com.example.forumproject.models.dtos;

import java.util.List;

public class FullProfileAdminDto {

    private int id;
    private String first_name;
    private String last_name;
    private String username;
    private String email;
    private String account_type;
    private String account_status;
    private String profile_photo;
    private String phone_number;
    private List<PostOutDto> user_posts;

    public FullProfileAdminDto(int id, String first_name, String last_name, String username, String email, String account_type, String account_status, String profile_photo, String phone_number) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.account_type = account_type;
        this.account_status = account_status;
        this.profile_photo = profile_photo;
        this.phone_number = phone_number;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public List<PostOutDto> getUser_posts() {
        return user_posts;
    }

    public void setUser_posts(List<PostOutDto> user_posts) {
        this.user_posts = user_posts;
    }
}


