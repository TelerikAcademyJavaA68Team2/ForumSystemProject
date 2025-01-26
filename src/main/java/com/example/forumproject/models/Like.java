package com.example.forumproject.models;

import jakarta.persistence.*;

@Entity
@Table(name = "post_likes_dislikes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_like")
    private boolean isLike;

    public Like() {
    }

    public Like(Post post, User user, boolean isLike) {
        this.post = post;
        this.user = user;
        this.isLike = isLike;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getLiked() {
        return isLike;
    }

    public void setLiked(boolean like) {
        isLike = like;
    }
}
