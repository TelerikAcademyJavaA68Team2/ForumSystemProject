package com.example.forumproject.repositories;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.User;
import com.example.forumproject.models.filterOptions.UsersFilterOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public Long getNumberOfRegisteredUsers() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u", Long.class);
            return query.uniqueResult() != null ? query.uniqueResult() : 0;
        }
    }

    @Override
    public List<User> getAllUsers(UsersFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder sb = new StringBuilder("FROM User u ");

            if (filterOptions.getFirst_name().isPresent() ||
                    filterOptions.getUsername().isPresent() || filterOptions.getEmail().isPresent() ||
                    filterOptions.getMinPosts().isPresent() || filterOptions.getMaxPosts().isPresent() ||
                    filterOptions.getAccount_type().isPresent() || filterOptions.getAccount_status().isPresent()) {

                sb.append("WHERE ");
                filterOptions.getFirst_name().ifPresent(first_name -> {
                    sb.append("u.firstName like :first_name ");
                    sb.append("AND ");
                });

                filterOptions.getUsername().ifPresent(username -> {
                    sb.append("u.username like :username ");
                    sb.append("AND ");
                });

                filterOptions.getEmail().ifPresent(email -> {
                    sb.append("u.email like :email ");
                    sb.append("AND ");
                });

                filterOptions.getMinPosts().ifPresent(minPosts -> {
                    sb.append("(SELECT COUNT(p) FROM Post p WHERE p.author.id = u.id) >= :minPosts ");
                    sb.append("AND ");
                });

                filterOptions.getMaxPosts().ifPresent(maxPosts -> {
                    sb.append("(SELECT COUNT(p) FROM Post p WHERE p.author.id = u.id) <= :maxPosts ");
                    sb.append("AND ");
                });

                filterOptions.getAccount_type().ifPresent(accountType -> {
                    if (accountType.equalsIgnoreCase("admin")) {
                        sb.append("u.isAdmin = true  ");
                        sb.append("AND ");
                    } else if (accountType.equalsIgnoreCase("user")) {
                        sb.append("u.isAdmin = false  ");
                        sb.append("AND ");
                    }
                });

                filterOptions.getAccount_status().ifPresent(accountStatus -> {
                    if (accountStatus.equalsIgnoreCase("active")) {
                        sb.append("u.isBlocked = false  ");
                        sb.append("AND ");
                    } else if (accountStatus.equalsIgnoreCase("blocked")) {
                        sb.append("u.isBlocked = true  ");
                        sb.append("AND ");
                    }
                });
                sb.setLength(sb.length() - 4);
            }

            // all fields
            if (filterOptions.getOrderBy().isPresent()) {
                String orderBy = filterOptions.getOrderBy().get();

                if ("first_name".equalsIgnoreCase(orderBy)) {
                    sb.append("ORDER BY u.firstName ");
                } else if ("email".equalsIgnoreCase(orderBy)) {
                    sb.append("ORDER BY u.email ");
                } else if ("account_status".equalsIgnoreCase(orderBy)) {
                    sb.append("ORDER BY u.isBlocked ");
                } else if ("account_type".equalsIgnoreCase(orderBy)) {
                    sb.append("ORDER BY u.isAdmin ");
                } else if ("posts".equalsIgnoreCase(orderBy)) {
                    sb.append("ORDER BY (SELECT COUNT(p) FROM Post p WHERE p.author.id = u.id) ");
                } else if ("user_id".equalsIgnoreCase(orderBy)) {
                    sb.append("ORDER BY u.id ");
                } else if ("username".equalsIgnoreCase(orderBy)) {
                    sb.append("ORDER BY u.username ");
                }
            }

            if (filterOptions.getOrderType().isPresent() && filterOptions.getOrderBy().isPresent()) {
                sb.append(filterOptions.getOrderType().get().equalsIgnoreCase("desc") ? "DESC" : "ASC");
            }

            Query<User> query = session.createQuery(sb.toString(), User.class);
            filterOptions.getFirst_name().ifPresent(firstName -> query.setParameter("first_name", "%" + firstName + "%"));
            filterOptions.getUsername().ifPresent(username -> query.setParameter("username", "%" + username + "%"));
            filterOptions.getEmail().ifPresent(email -> query.setParameter("email", "%" + email + "%"));
            filterOptions.getMinPosts().ifPresent(minPosts -> query.setParameter("minPosts", minPosts));
            filterOptions.getMaxPosts().ifPresent(maxPosts -> query.setParameter("maxPosts", maxPosts));

            return query.list();
        }
    }

    @Override
    public void save(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }catch (Exception e){
            throw new DuplicateEntityException("Email already");
        }
    }

    @Override
    public User getById(Long userId) {
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
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid username!"));
        }
    }

    @Override
    public void deleteUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
        }
    }
}