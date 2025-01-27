package com.example.forumproject.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class PostOutDto {

    private int post_id;

    private String author;

    private String title;

    private String content;

    private int likes;

    private int dislikes;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CommentDto> comments;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> tags;

    public PostOutDto() {
    }

    public PostOutDto(int dislikes, int likes, List<String> tags, List<CommentDto> comments, String content, String title, String author, int post_id) {
        this.dislikes = dislikes;
        this.likes = likes;
        this.tags = tags;
        this.comments = comments;
        this.content = content;
        this.title = title;
        this.author = author;
        this.post_id = post_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
