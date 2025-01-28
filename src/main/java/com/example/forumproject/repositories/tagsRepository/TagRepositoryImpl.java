package com.example.forumproject.repositories.tagsRepository;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.PostTag;
import com.example.forumproject.models.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;

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
            String sql = "SELECT COUNT(*) FROM post_tags WHERE post_id = :post_id AND tag_id = :tag_id";
            Query<Long> query = session.createNativeQuery(sql, Long.class);
            query.setParameter("post_id", post_id);
            query.setParameter("tag_id", tag_id);

            return query.uniqueResult() != null;
        }
    }

    @Override
    public Tag getTagByTagId(Long tagId) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT * FROM tags WHERE id = :tagId";
            Query<Tag> query = session.createNativeQuery(sql, Tag.class);
            query.setParameter("tagId", tagId);
            return query.uniqueResultOptional().orElseThrow(() ->
                    new EntityNotFoundException("Tag ", tagId));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching tag by ID", e);
        }
    }

    @Override
    public Tag getTagByName(String tagName) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT * FROM tags WHERE name = :tagName";
            Query<Tag> query = session.createNativeQuery(sql, Tag.class);
            query.setParameter("tagName", tagName.toLowerCase(Locale.ROOT));
            return query.uniqueResultOptional().orElseThrow(() ->
                    new EntityNotFoundException("Tag","name", tagName));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching tag by name", e);
        }
    }

    @Override
    public void addTagToPost(Long postId, Long tagId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String sql = "INSERT INTO post_tags (post_id, tag_id) VALUES (:postId, :tagId)";
            Query<PostTag> query = session.createNativeQuery(sql, PostTag.class);
            query.setParameter("postId", postId);
            query.setParameter("tagId", tagId);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add tag to post", e);
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
