package com.example.forumproject.models.dtos.postDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class UpdatePostDto {

    @Positive(message = "Id should be positive")
    private int id;

    @NotBlank(message = "Title can't be blank")
    @Size(min = 16, max = 64, message = "Title should be between 16 and 64 symbols")
    private String title;

    @NotBlank(message = "Content can't be blank")
    @Size(min = 32, max = 8192, message = "Content should be between 32 and 8192 symbols")
    private String content;

    public UpdatePostDto() {
    }

    public UpdatePostDto(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

