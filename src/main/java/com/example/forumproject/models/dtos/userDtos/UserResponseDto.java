package com.example.forumproject.models.dtos.userDtos;

public class UserResponseDto {

    private Long user_id;
    private String first_name;
    private String username;
    private String email;
    private Long numberOfPosts;
    private String account_type;
    private String account_status;


    public UserResponseDto(Long user_id, String first_name, String username, String email, String account_type, String account_status) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.username = username;
        this.email = email;
        this.account_type = account_type;
        this.account_status = account_status;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
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

    public Long getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(Long numberOfPosts) {
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
