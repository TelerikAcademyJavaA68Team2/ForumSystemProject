package com.example.forumproject.repositories.postTagRepository;

import com.example.forumproject.models.PostTag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PostTagRepositoryImpl implements PostTagRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostTagRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addPostTag(PostTag postTag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(postTag);
            session.getTransaction().commit();
        }
    }
}

