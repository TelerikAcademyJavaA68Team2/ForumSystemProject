package com.example.forumproject.repositories.implementation;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.Comment;
import com.example.forumproject.repositories.contracts.CommentRepository;
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
    public List<Comment> getAll(int postId) {
        try(Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery(
                    "FROM Comment c WHERE c.post.id = :postId",
                    Comment.class
            );
            query.setParameter("postId", postId);

            List<Comment> comments = query.list();

            if (comments.isEmpty()) {
                throw new EntityNotFoundException("Comments", "post", postId);
            }
            return comments;
        }

    }

    @Override
    public Comment getById(int postId, int id) {
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
    public List<Comment> getCommentsByAuthor(int postId, int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery(
                    "from Comment WHERE post.id = :postId AND author.id = :userId",
                    Comment.class
            );
            query.setParameter("postId", postId);
            query.setParameter("userId", userId);
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Comments", userId, postId);
            }
            return query.list();
        }
    }

    @Override
    public void create(int postId, Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(int postId, Comment comment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int postId, int id) {
        Comment commentToDelete = getById(postId, id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(commentToDelete);
            session.getTransaction().commit();
        }
    }
}
