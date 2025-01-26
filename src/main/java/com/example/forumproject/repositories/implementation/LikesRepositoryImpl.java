package com.example.forumproject.repositories.implementation;

import com.example.forumproject.models.Like;
import com.example.forumproject.models.Post;
import com.example.forumproject.repositories.contracts.LikesRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikesRepositoryImpl implements LikesRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public LikesRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public int getLikesByPostId(int post_id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(pld) FROM Like pld WHERE pld.post.id = :postId AND pld.isLike = true";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("postId", post_id);
            return query.getSingleResult().intValue();
        }
    }

    @Override
    public int getDislikesByPostId(int post_id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(pld) FROM Like pld WHERE pld.post.id = :postId AND pld.isLike = false ";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("postId", post_id);
            return query.getSingleResult().intValue();
        }
    }

    @Override
    public boolean checkIfLikeExists(int post_id, int user_id) {
        try (Session session = sessionFactory.openSession()) {
            String query = "FROM Like lk WHERE lk.user.id = :userId AND lk.post.id = :postId AND lk.isLike = true";
            Query<Like> likes = session.createQuery(query, Like.class);
            likes.setParameter("userId", user_id);
            likes.setParameter("postId", post_id);
            return likes.uniqueResult() != null;
        }
    }

    @Override
    public boolean checkIfDislikeExists(int post_id, int user_id) {
        try (Session session = sessionFactory.openSession()) {

            String query = "FROM Like lk WHERE lk.user.id = :userId AND lk.post.id = :postId AND lk.isLike = false ";
            Query<Like> likes = session.createQuery(query, Like.class);
            likes.setParameter("userId", user_id);
            likes.setParameter("postId", post_id);

            return likes.uniqueResult() != null;
        }
    }


    @Override
    public List<Post> getAllLikedPosts(int user_id) {
        try (Session session = sessionFactory.openSession()) {
            String queryString = "SELECT post FROM Post post " +
                    "JOIN Like like ON post.id = like.post.id " +
                    "WHERE like.user.id = :userId AND like.isLike = true";
            Query<Post> query = session.createQuery(queryString, Post.class);
            query.setParameter("userId", user_id);
            return query.list();
        }

    }

    @Override
    public List<Post> getAllDislikedPosts(int user_id) {
        try (Session session = sessionFactory.openSession()) {
            String queryString = "SELECT post FROM Post post " +
                    "JOIN Like like ON post.id = like.post.id " +
                    "WHERE like.user.id = :userId AND like.isLike = false ";
            Query<Post> query = session.createQuery(queryString, Post.class);
            query.setParameter("userId", user_id);
            return query.list();
        }
    }

    @Override
    public void create(Like like) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(like);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Like like) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Like exsistingLike = getLike(like.getPost().getId(), like.getUser().getId());
            exsistingLike.setLiked(like.getLiked());
            session.merge(exsistingLike);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Like like) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getLike(like.getPost().getId(), like.getUser().getId()));
            session.getTransaction().commit();
        }
    }


    public Like getLike(int post_id, int user_id) {
        try (Session session = sessionFactory.openSession()) {

            Query<Like> likes = session.createQuery("FROM Like l WHERE l.post.id = :postId AND l.user.id = :userId", Like.class);
            likes.setParameter("postId", post_id);
            likes.setParameter("userId", user_id);

            return likes.uniqueResult();
        }
    }
}

