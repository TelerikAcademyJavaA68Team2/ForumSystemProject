package com.example.forumproject.models;

import java.util.Optional;

public class FilterOptions {
    private Optional<String> likes;
    private Optional<String> dislikes;
    private Optional<String> title;

    public FilterOptions(String likes, String dislikes, String title) {
        this.likes = Optional.ofNullable(likes);
        this.dislikes = Optional.ofNullable(dislikes);
        this.title = Optional.ofNullable(title);
    }

    public Optional<String> getLikes() {
        return likes;
    }

    public Optional<String> getDislikes() {
        return dislikes;
    }

    public Optional<String> getTitle() {
        return title;
    }
}
