package com.example.forumproject.repositories.implementation;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;
import com.example.forumproject.repositories.contracts.TagRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean checkIfPostIsTagged(Long post_id, Long tag_id) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT COUNT(*) FROM forum_management_system.post_tags WHERE post_id = :post_id AND tag_id = :tag_id";
            Query<Long> query = session.createNativeQuery(sql, Long.class);
            query.setParameter("post_id", post_id);
            query.setParameter("tag_id", tag_id);

            return query.uniqueResult() != null;
        }
    }

    @Override
    public Tag getTag(Long post_id, Long tag_id) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT t.* FROM forum_management_system.post_tags pt " +
                    "JOIN forum_management_system.tags t ON pt.tag_id = t.id " +
                    "WHERE pt.post_id = :post_id AND pt.tag_id = :tag_id";
            Query<Tag> query = session.createNativeQuery(sql, Tag.class);
            query.setParameter("post_id", post_id);
            query.setParameter("tag_id", tag_id);

            return query.uniqueResult(); // can be null
        }
    }

    @Override
    public List<Tag> getTagsByPostId(Long post_id) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT t.* FROM forum_management_system.post_tags pt " +
                    "JOIN forum_management_system.tags t ON pt.tag_id = t.id " +
                    "WHERE pt.post_id = :post_id";
            Query<Tag> query = session.createNativeQuery(sql, Tag.class);
            query.setParameter("post_id", post_id);

            return query.list();
        }
    }

    @Override
    public List<Post> getAllPostsByTag(Long tag_id) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT p.* FROM forum_management_system.post_tags pt " +
                    "JOIN forum_management_system.posts p ON pt.post_id = p.id " +
                    "WHERE pt.tag_id = :tag_id";
            Query<Post> query = session.createNativeQuery(sql, Post.class);
            query.setParameter("tag_id", tag_id);

            return query.getResultList();
        }
    }

    @Override
    public void create(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(tag);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(tag);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(tag);
            session.getTransaction().commit();
        }
    }
}
