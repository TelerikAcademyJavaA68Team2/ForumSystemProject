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

    public PostFilterOptions(String title, String content, String tag,
                             Long minLikes, Long maxLikes,
                             String orderBy, String orderType, String author) {
        this.title = Optional.ofNullable(title);
        this.content = Optional.ofNullable(content);
        this.tag = Optional.ofNullable(tag);
        this.minLikes = Optional.ofNullable(minLikes);
        this.maxLikes = Optional.ofNullable(maxLikes);
        this.orderBy = Optional.ofNullable(orderBy);
        this.orderType = Optional.ofNullable(orderType);
        this.author = Optional.ofNullable(author);
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