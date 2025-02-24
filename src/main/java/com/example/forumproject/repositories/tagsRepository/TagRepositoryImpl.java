package com.example.forumproject.repositories.tagsRepository;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.Tag;
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
    public List<Tag> getAllTags(){
        try (Session session = sessionFactory.openSession()) {
            Query<Tag> query = session.createQuery("from Tag", Tag.class);
            return query.list();
        }
    }

    @Override
    public Tag getTagById(Long tagId) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT * FROM tags t WHERE id = :tagId";
            Query<Tag> query = session.createNativeQuery(sql, Tag.class);
            query.setParameter("tagId", tagId);
            return query.uniqueResultOptional().orElseThrow(() ->
                    new EntityNotFoundException("Tag ", tagId));
        }
    }

    @Override
    public Tag getTagByName(String tagName) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT * FROM tags WHERE name = :tagName";
            Query<Tag> query = session.createNativeQuery(sql, Tag.class);
            query.setParameter("tagName", tagName.toLowerCase());
            return query.uniqueResultOptional().orElseThrow(() ->
                    new EntityNotFoundException("Tag", "name", tagName));
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
