package com.example.forumproject.repositories.implementation;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public int getNumberOfActiveUsers() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u", Long.class);
            return query.uniqueResult() != null ? query.uniqueResult().intValue() : 0;
        }
    }


    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("From User", User.class).list();
        }
    }

    @Override
    public void save(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public User getById(int userId) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, userId);
            if (user == null) {
                throw new EntityNotFoundException("User", userId);
            }
            return user;
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> users = session.createQuery("From User Where email = :email", User.class);
            users.setParameter("email", email);
            return users
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("User", "email", email));
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> users = session.createQuery("From User Where username = :username", User.class);
            users.setParameter("username", username);
            return users
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("User", "username", username));
        }
    }


    @Override
    public void deleteUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getById(userId));
            session.getTransaction().commit();
        }
    }

    @Override
    public void promoteToAdmin(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = getById(userId);
            user.setAdmin(true);
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void blockUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = getById(userId);
            user.setBlocked(true);
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void unblockUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = getById(userId);
            user.setBlocked(false);
            session.merge(user);
            session.getTransaction().commit();
        }
    }
}
