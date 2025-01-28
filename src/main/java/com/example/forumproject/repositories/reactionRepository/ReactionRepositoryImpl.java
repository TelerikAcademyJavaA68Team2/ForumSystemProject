package com.example.forumproject.repositories.reactionRepository;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.Reaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReactionRepositoryImpl implements ReactionRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public ReactionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long getLikesByPostId(Long post_id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(pld) FROM Reaction pld WHERE pld.post.id = :postId AND pld.isLike = true";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("postId", post_id);
            return query.uniqueResult() != null ? query.uniqueResult() : 0;
        }
    }

    @Override
    public Long getDislikesByPostId(Long post_id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(pld) FROM Reaction pld WHERE pld.post.id = :postId AND pld.isLike = false ";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("postId", post_id);
            return query.uniqueResult() != null ? query.uniqueResult() : 0;
        }
    }

    @Override
    public boolean checkIfLikeExists(Long post_id, Long user_id) {
        try (Session session = sessionFactory.openSession()) {
            String query = "FROM Reaction lk WHERE lk.user.id = :userId AND lk.post.id = :postId AND lk.isLike = true";
            Query<Reaction> likes = session.createQuery(query, Reaction.class);
            likes.setParameter("userId", user_id);
            likes.setParameter("postId", post_id);
            return likes.uniqueResult() != null;
        }
    }

    @Override
    public boolean checkIfDislikeExists(Long post_id, Long user_id) {
        try (Session session = sessionFactory.openSession()) {
            String query = "FROM Reaction lk WHERE lk.user.id = :userId AND lk.post.id = :postId AND lk.isLike = false ";
            Query<Reaction> likes = session.createQuery(query, Reaction.class);
            likes.setParameter("userId", user_id);
            likes.setParameter("postId", post_id);
            return likes.uniqueResult() != null;
        }
    }


    @Override
    public List<Post> getAllLikedPosts(Long user_id) {
        try (Session session = sessionFactory.openSession()) {
            String queryString = "SELECT post FROM Post post " +
                    "JOIN Reaction like ON post.id = like.post.id " +
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
                    "JOIN Reaction like ON post.id = like.post.id " +
                    "WHERE like.user.id = :userId AND like.isLike = false ";
            Query<Post> query = session.createQuery(queryString, Post.class);
            query.setParameter("userId", user_id);
            return query.list();
        }
    }

    @Override
    public void create(Reaction like) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(like);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Reaction reaction) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Reaction exsistingLike = getLike(reaction.getPost().getId(), reaction.getUser().getId());
            exsistingLike.setReaction(reaction.getReaction());
            session.merge(exsistingLike);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Reaction like) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getLike(like.getPost().getId(), like.getUser().getId()));
            session.getTransaction().commit();
        }
    }


    public Reaction getLike(Long post_id, Long user_id) {
        try (Session session = sessionFactory.openSession()) {

            Query<Reaction> likes = session.createQuery("FROM Reaction l WHERE l.post.id = :postId AND l.user.id = :userId", Reaction.class);
            likes.setParameter("postId", post_id);
            likes.setParameter("userId", user_id);

            return likes.uniqueResult();
        }
    }
}

