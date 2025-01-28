package com.example.forumproject.models.filterOptions;

import java.util.Optional;

public class UsersFilterOptions {

    private Optional<String> first_name;
    private Optional<String> username;
    private Optional<String> email;
    private Optional<Long> minPosts;
    private Optional<Long> maxPosts;
    private Optional<String> account_type;
    private Optional<String> account_status;
    private Optional<String> orderBy;
    private Optional<String> orderType;

    public UsersFilterOptions( String first_name,
                              String username, String email,
                              Long min_number_of_posts, Long max_number_of_posts,
                              String account_type, String account_status,
                              String orderBy, String orderType) {
        this.first_name = Optional.ofNullable(first_name);
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
        this.minPosts = Optional.ofNullable(min_number_of_posts);
        this.maxPosts = Optional.ofNullable(max_number_of_posts);
        this.account_type = Optional.ofNullable(account_type);
        this.account_status = Optional.ofNullable(account_status);
        this.orderBy = Optional.ofNullable(orderBy);
        this.orderType = Optional.ofNullable(orderType);
    }

    public Optional<String> getFirst_name() {
        return first_name;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<Long> getMinPosts() {
        return minPosts;
    }

    public Optional<Long> getMaxPosts() {
        return maxPosts;
    }

    public Optional<String> getAccount_type() {
        return account_type;
    }

    public Optional<String> getAccount_status() {
        return account_status;
    }

    public Optional<String> getOrderBy() {
        return orderBy;
    }

    public Optional<String> getOrderType() {
        return orderType;
    }
}
