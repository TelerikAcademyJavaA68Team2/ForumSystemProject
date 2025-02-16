package com.example.forumproject.models.dtos.postDtos;

public class PostFilterDto {

    private String title;
    private String content;
    private String tag;
    private Long minLikes;
    private Long maxLikes;
    private String orderBy;
    private String orderType;
    private String author;

    public PostFilterDto() {
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getMinLikes() {
        return minLikes;
    }

    public void setMinLikes(Long minLikes) {
        this.minLikes = minLikes;
    }

    public Long getMaxLikes() {
        return maxLikes;
    }

    public void setMaxLikes(Long maxLikes) {
        this.maxLikes = maxLikes;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
