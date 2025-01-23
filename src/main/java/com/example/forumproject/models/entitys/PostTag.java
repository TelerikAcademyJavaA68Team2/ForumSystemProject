package com.example.forumproject.models.entitys;

import jakarta.persistence.*;


@Table(name = "post_tags")
public class PostTag {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public PostTag() {
    }

    public PostTag(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
    }


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
