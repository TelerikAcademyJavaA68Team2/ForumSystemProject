package com.example.forumproject.repositories.implementation;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.PostLikesDislikes;
import com.example.forumproject.repositories.contracts.LikesRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikeRepositoryImpl implements LikesRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public LikeRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long getLikesByPostId(Long post_id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(pld) FROM PostLikesDislikes pld WHERE pld.post.id = :postId AND pld.isLike = true";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("postId", post_id);
            return query.uniqueResult() != null ? query.uniqueResult() : 0;
        }
    }

    @Override
    public Long getDislikesByPostId(Long post_id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(pld) FROM PostLikesDislikes pld WHERE pld.post.id = :postId AND pld.isLike = false ";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("postId", post_id);
            return query.uniqueResult() != null ? query.uniqueResult() : 0;
        }
    }

    @Override
    public boolean checkIfLikeExists(Long post_id, Long user_id) {
        try (Session session = sessionFactory.openSession()) {
            String query = "FROM PostLikesDislikes lk WHERE lk.user.id = :userId AND lk.post.id = :postId AND lk.isLike = true";
            Query<PostLikesDislikes> likes = session.createQuery(query, PostLikesDislikes.class);
            likes.setParameter("userId", user_id);
            likes.setParameter("postId", post_id);
            return likes.uniqueResult() != null;
        }
    }

    @Override
    public boolean checkIfDislikeExists(Long post_id, Long user_id) {
        try (Session session = sessionFactory.openSession()) {
            String query = "FROM PostLikesDislikes lk WHERE lk.user.id = :userId AND lk.post.id = :postId AND lk.isLike = false ";
            Query<PostLikesDislikes> likes = session.createQuery(query, PostLikesDislikes.class);
            likes.setParameter("userId", user_id);
            likes.setParameter("postId", post_id);
            return likes.uniqueResult() != null;
        }
    }


    @Override
    public List<Post> getAllLikedPosts(Long user_id) {
        try (Session session = sessionFactory.openSession()) {
            String queryString = "SELECT post FROM Post post " +
                    "JOIN PostLikesDislikes like ON post.id = like.post.id " +
                    "WHERE like.user.id = :userId AND like.isLike = true";
            Query<Post> query = session.createQuery(queryString, Post.class);
            query.setParameter("userId", user_id);
            return query.list();
        }

    }

    @Override
    public List<Post> getAllDislikedPosts(Long user_id) {
        try (Session session = sessionFactory.openSession()) {
            String queryString = "SELECT post FROM Post post " +
                    "JOIN PostLikesDislikes like ON post.id = like.post.id " +
                    "WHERE like.user.id = :userId AND like.isLike = false ";
            Query<Post> query = session.createQuery(queryString, Post.class);
            query.setParameter("userId", user_id);
            return query.list();
        }
    }

    @Override
    public void create(PostLikesDislikes like) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(like);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(PostLikesDislikes like) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            PostLikesDislikes exsistingLike = getLike(like.getPost().getId(), like.getUser().getId());
            exsistingLike.setLiked(like.getLiked());
            session.merge(exsistingLike);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(PostLikesDislikes like) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getLike(like.getPost().getId(), like.getUser().getId()));
            session.getTransaction().commit();
        }
    }


    public PostLikesDislikes getLike(Long post_id, Long user_id) {
        try (Session session = sessionFactory.openSession()) {

            Query<PostLikesDislikes> likes = session.createQuery("FROM PostLikesDislikes l WHERE l.post.id = :postId AND l.user.id = :userId", PostLikesDislikes.class);
            likes.setParameter("postId", post_id);
            likes.setParameter("userId", user_id);

            return likes.uniqueResult();
        }
    }
}

