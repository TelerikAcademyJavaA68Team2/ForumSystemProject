package com.example.forumproject.models.dtos.tagDtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TagInDto {

    @NotNull
    @Size(min = 3, max = 15, message = "Tag should be between 3 and 15 symbols")
    private String tagName;

    public TagInDto() {
    }

    public TagInDto(String tagName) {
        this.tagName = tagName.toLowerCase();
    }

    public String getTagName() {
        return tagName.toLowerCase();
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}

