package com.example.forumproject.models.filterOptions;

import java.util.Optional;

public class PostFilterOptions {

    private Optional<String> title;
    private Optional<String> content;
    private Optional<String> tags;
    private Optional<Long> minLikes;
    private Optional<Long> maxLikes;
    private Optional<String> orderBy;
    private Optional<String> orderType;

    public PostFilterOptions(String title, String content, String tags, Long minLikes, Long maxLikes, String orderBy, String orderType) {
        this.title = Optional.ofNullable(title);
        this.content = Optional.ofNullable(content);
        this.tags = Optional.ofNullable(tags);
        this.minLikes = Optional.ofNullable(minLikes);
        this.maxLikes = Optional.ofNullable(maxLikes);
        this.orderBy = Optional.ofNullable(orderBy);
        this.orderType = Optional.ofNullable(orderType);
    }

    public Optional<String> getTitle() {
        return this.title;
    }

    public Optional<String> getContent() {
        return this.content;
    }

    public Optional<String> getTags() {
        return this.tags;
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
