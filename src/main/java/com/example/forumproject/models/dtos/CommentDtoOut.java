package com.example.forumproject.models.dtos;

public class CommentDtoOut {

    private int comment_id;

    private String author;

    private String content;

    public CommentDtoOut() {
    }

    public CommentDtoOut(int comment_id, String author, String content) {
        this.comment_id = comment_id;
        this.author = author;
        this.content = content;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
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

