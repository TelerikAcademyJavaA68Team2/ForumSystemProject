package com.example.forumproject.repositories.postTagRepository;

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

@Repository
public class PostTagRepositoryImpl implements PostTagRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostTagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
    public List<Post> getAllPostsByTagId(Long tag_id) {
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
    public boolean checkIfPostIsTagged(Long post_id, Long tag_id) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT COUNT(*) FROM forum_management_system.post_tags WHERE post_id = :post_id AND tag_id = :tag_id";
            Query<Long> query = session.createNativeQuery(sql, Long.class);
            query.setParameter("post_id", post_id);
            query.setParameter("tag_id", tag_id);

            Long count = query.uniqueResult();
            return count != null && count > 0;
        }
    }

    public PostTag getPostTag(Long postId, Long tagId) {
        try (Session session = sessionFactory.openSession()) {
            String sql = "FROM PostTag pt WHERE pt.post.id = :post_id AND pt.tag.id = :tag_id";
            Query<PostTag> query = session.createQuery(sql, PostTag.class);
            query.setParameter("post_id", postId);
            query.setParameter("tag_id", tagId);
            PostTag postTag = query.uniqueResult();
            if (postTag == null) {
                throw new EntityNotFoundException("PostTag with postId " + postId + " and tagId " + tagId + " not found.");
            }
            return postTag;
        }
    }

    @Override
    public void create(PostTag postTag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(postTag);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(PostTag postTag, Tag newTag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            PostTag newPostTag = new PostTag(postTag.getPost(), newTag);
            session.remove(postTag);
            session.merge(newPostTag);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(PostTag postTag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(postTag);
            session.getTransaction().commit();
        }
    }
}

