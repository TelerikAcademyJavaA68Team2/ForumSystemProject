package com.example.forumproject.models.dtos.tagDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class TagInDto {

    @NotBlank
    @Size(min = 3, max = 30, message = "Tag should be between 3 and 30 symbols")
    private String tagName;

    private List<String> tagsList;

    public TagInDto() {
    }

    public TagInDto(List<String> tagsList) {
        this.tagsList = tagsList;
    }

    public TagInDto(List<String> tagsList, String tagName) {
        this.tagsList = tagsList;
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<String> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<String> tagsList) {
        this.tagsList = tagsList;
    }
}

