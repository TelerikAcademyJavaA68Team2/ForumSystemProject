package com.example.forumproject.repositorys;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public void createUser(User user) {

    }

    @Override
    public User getById(int userId) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, userId);
            if (user == null) {
                throw new EntityNotFoundException("Beer", userId);
            }
            return user;
        }
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public User getByUsername(String username) {
        return null;
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(int userId) {

    }

    @Override
    public void promoteToAdmin(int userId) {

    }

    @Override
    public void blockUser(int userId) {

    }

    @Override
    public void unblockUser(int userId) {

    }
}
