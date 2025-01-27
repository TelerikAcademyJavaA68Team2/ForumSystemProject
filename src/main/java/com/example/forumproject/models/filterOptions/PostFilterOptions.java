package com.example.forumproject.models.filterOptions;

import java.util.Optional;

public class PostFilterOptions {

    private Optional<String> title;
    private Optional<String> content;
    private Optional<String> tags;
    private Optional<Long> minLikes;
    private Optional<Long> maxLikes;
    private Optional<String> sortBy;
    private Optional<String> orderBy;

    public PostFilterOptions(String title, String content, String tags, Long minLikes, Long maxLikes, String sortBy, String orderBy) {
        this.title = Optional.ofNullable(title);
        this.content = Optional.ofNullable(content);
        this.tags = Optional.ofNullable(tags);
        this.minLikes = Optional.ofNullable(minLikes);
        this.maxLikes = Optional.ofNullable(maxLikes);
        this.sortBy = Optional.ofNullable(sortBy);
        this.orderBy = Optional.ofNullable(orderBy);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getContent() {
        return content;
    }

    public Optional<String> getTags() {
        return tags;
    }

    public Optional<Long> getMinLikes() {
        return minLikes;
    }

    public Optional<Long> getMaxLikes() {
        return maxLikes;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getOrderBy() {
        return orderBy;
    }
}
