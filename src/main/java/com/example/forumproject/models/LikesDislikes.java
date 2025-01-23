package com.example.forumproject.models;

import jakarta.persistence.*;

@Entity
@Table(name = "post_likes_dislikes")
public class LikesDislikes {

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


    public LikesDislikes() {

    }


}
