package com.example.forumproject.repositories.commentsRepository;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.Comment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Comment> getAllComments(Long postId) {
        List<Comment> allComments = getAllCommentsByPostIdForMapper(postId);
        if (allComments.isEmpty()) {
            throw new EntityNotFoundException("Comments", "post", postId);
        }
        return allComments;
    }

    @Override
    public Comment getById(Long postId, Long id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery(
                    "FROM Comment c WHERE c.post.id = :postId AND c.id = :id",
                    Comment.class
            );
            query.setParameter("postId", postId);
            query.setParameter("id", id);
            return query.uniqueResultOptional()
                    .orElseThrow(() -> new EntityNotFoundException("Comment", id));
        }
    }

    @Override
    public List<Comment> getAllCommentsByPostIdForMapper(Long postId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery(
                    " from Comment WHERE post.id = :postId ",
                    Comment.class
            );
            query.setParameter("postId", postId);
            return query.list();
        }
    }

    @Override
    public void create(Long postId, Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Long postId, Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Long postId, Long id) {
        Comment commentToDelete = getById(postId, id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(commentToDelete);
            session.getTransaction().commit();
        }
    }
}
