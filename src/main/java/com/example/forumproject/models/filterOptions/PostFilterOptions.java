package com.example.forumproject.models.filterOptions;

import java.util.Optional;

public class PostFilterOptions {

    private Optional<String> title;
    private Optional<String> content;
    private Optional<String> tag;
    private Optional<Long> minLikes;
    private Optional<Long> maxLikes;
    private Optional<String> orderBy;
    private Optional<String> orderType;
    private Optional<String> author;

    public PostFilterOptions() {
        this.title = Optional.empty();
        this.content = Optional.empty();
        this.tag = Optional.empty();
        this.minLikes = Optional.empty();
        this.maxLikes = Optional.empty();
        this.orderBy = Optional.empty();
        this.orderType = Optional.empty();
        this.author = Optional.empty();
    }

    public PostFilterOptions(String title, String content, String tag,
                             Long minLikes, Long maxLikes,
                             String orderBy, String orderType, String author) {
        this.title = sanitizeOptional(title);
        this.content = sanitizeOptional(content);
        this.tag = sanitizeOptional(tag);
        this.minLikes = Optional.ofNullable(minLikes);
        this.maxLikes = Optional.ofNullable(maxLikes);
        this.orderBy = sanitizeOptional(orderBy);
        this.orderType = sanitizeOptional(orderType);
        this.author = sanitizeOptional(author);
    }

    public PostFilterOptions(String tag) {
        this.title = Optional.empty();
        this.content = Optional.empty();
        this.tag = sanitizeOptional(tag);
        this.minLikes = Optional.empty();
        this.maxLikes = Optional.empty();
        this.orderBy = Optional.empty();
        this.orderType = Optional.empty();
        this.author = Optional.empty();
    }

    private Optional<String> sanitizeOptional(String value) {
        return (value == null || value.trim().isEmpty()) ? Optional.empty() : Optional.of(value);
    }

    public Optional<String> getUsername() {
        return author;
    }

    public Optional<String> getTitle() {
        return this.title;
    }

    public Optional<String> getContent() {
        return this.content;
    }

    public Optional<String> getTag() {
        return this.tag;
    }

    public Optional<Long> getMinLikes() {
        return this.minLikes;
    }

    public Optional<Long> getMaxLikes() {
        return this.maxLikes;
    }

    public Optional<String> getOrderType() {
        return this.orderType;
    }

    public Optional<String> getOrderBy() {
        return this.orderBy;
    }
}