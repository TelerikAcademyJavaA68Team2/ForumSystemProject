package com.example.forumproject.models.dtos.commentDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentOutDto {

    private String author;

    @NotNull(message = "Content can't be empty")
    @Size(min = 1, max = 2048, message = "Content should be between 1 and 2048 symbols")
    private String content;

    private Long commentId;

    private String authorProfilePicture;

    public CommentOutDto() {
    }

    public CommentOutDto(String content, Long authorId) {
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

    public String getAuthorProfilePicture() {
        return authorProfilePicture;
    }

    public void setAuthorProfilePicture(String authorProfilePicture) {
        this.authorProfilePicture = authorProfilePicture;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
