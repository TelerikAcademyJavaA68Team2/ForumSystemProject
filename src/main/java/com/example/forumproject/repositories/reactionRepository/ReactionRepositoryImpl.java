package com.example.forumproject.repositories.reactionRepository;

import com.example.forumproject.models.Reaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
            Reaction existingLike = getLike(reaction.getPost().getId(), reaction.getUser().getId());
            existingLike.setReaction(reaction.getReaction());
            session.merge(existingLike);
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