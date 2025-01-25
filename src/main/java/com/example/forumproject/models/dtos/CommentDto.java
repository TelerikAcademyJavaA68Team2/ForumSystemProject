package com.example.forumproject.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentDto {

    @NotNull(message = "Content can't be empty")
    @Size(min = 1, max = 2048, message = "Content should be between 1 and 2048 symbols")
    private String content;

    public CommentDto() {
    }

    public CommentDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
