package com.example.forumproject.models.dtos;

public class UserOutDto {

    private int user_id;
    private String first_name;
    private String username;
    private String email;
    private int numberOfPosts;
    private String account_type;
    private String account_status;


    public UserOutDto(int user_id, String first_name, String username, String email, String account_type, String account_status) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.username = username;
        this.email = email;
        this.account_type = account_type;
        this.account_status = account_status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
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

    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
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
}
