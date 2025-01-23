package com.example.forumproject.models;

import java.util.Optional;

public class FilterOptions {
    private Optional<Integer> Minlikes;
    private Optional<Integer> Maxlikes;
    private Optional<String> title;

    public FilterOptions(Integer Minlikes, Integer Maxlikes, String title) {
        this.Minlikes = Optional.ofNullable(Minlikes);
        this.Maxlikes = Optional.ofNullable(Maxlikes);
        this.title = Optional.ofNullable(title);
    }

    public Optional<Integer> getMinlikes() {
        return Minlikes;
    }

    public Optional<Integer> getMaxlikes() {
        return Maxlikes;
    }

    public Optional<String> getTitle() {
        return title;
    }
}
