package com.example.forumproject.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentDto {

    @NotBlank(message = "Username can't be empty")
    @NotNull(message = "Username can't be empty")
    @Size(min = 2, max = 20, message = "username should be between 2 and 20 symbols!")
    private String author;

    @NotNull(message = "Content can't be empty")
    @Size(min = 1, max = 2048, message = "Content should be between 1 and 2048 symbols")
    private String content;

    public CommentDto() {
    }

    public CommentDto(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
