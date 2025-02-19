package com.example.forumproject.models.dtos.userDtos;

import jakarta.validation.constraints.Positive;

public class RequestUserFilterOptions {
    private String first_name;
    private String username;
    private String email;
    @Positive
    private Long minPosts;
    @Positive
    private Long maxPosts;
    private String account_type;
    private String account_status;
    private String orderBy;
    private String orderType;

    public RequestUserFilterOptions() {
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

    public Long getMinPosts() {
        return minPosts;
    }

    public void setMinPosts(Long minPosts) {
        this.minPosts = minPosts;
    }

    public Long getMaxPosts() {
        return maxPosts;
    }

    public void setMaxPosts(Long maxPosts) {
        this.maxPosts = maxPosts;
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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
