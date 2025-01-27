package com.example.forumproject.models.dtos;

import java.util.List;

public class HomepagePostsDto {

    private String pageContent;

    private List<PostOutDto> posts;

    public HomepagePostsDto(String pageContent) {
        this.pageContent = pageContent;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public List<PostOutDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostOutDto> posts) {
        this.posts = posts;
    }
}
