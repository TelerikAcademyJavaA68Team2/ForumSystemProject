package com.example.forumproject.repositories;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long getNumberOfPostsByUser(Long user_id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(p) FROM Post p WHERE p.author.id = :userId", Long.class);
            query.setParameter("userId", user_id);
            return query.uniqueResult() != null ? query.uniqueResult() : 0;
        }
    }

    @Override
    public Long getTotalNumberOfPosts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(p) FROM Post p", Long.class);
            return query.uniqueResult() != null ? query.uniqueResult() : 0;
        }
    }

    public List<Post> getAll(PostFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder sb = new StringBuilder("FROM Post p ");

            if (filterOptions.getTitle().isPresent() || filterOptions.getContent().isPresent() ||
                    filterOptions.getTag().isPresent() || filterOptions.getMinLikes().isPresent() ||
                    filterOptions.getMaxLikes().isPresent() || filterOptions.getUsername().isPresent()) {

                sb.append("WHERE ");

                filterOptions.getUsername().ifPresent(username -> {
                    sb.append("p.author.username LIKE :username ");
                    sb.append("AND ");
                });

                filterOptions.getTag().ifPresent(tag -> {
                    sb.append("p.id IN (SELECT pt.post.id FROM PostTag pt JOIN pt.tag t WHERE t.tagName LIKE :tag) ");
                    sb.append("AND ");
                });

                filterOptions.getTitle().ifPresent(title -> {
                    sb.append("p.title LIKE :title ");
                    sb.append("AND ");
                });

                filterOptions.getContent().ifPresent(content -> {
                    sb.append("p.content LIKE :content ");
                    sb.append("AND ");
                });


                filterOptions.getMinLikes().ifPresent(minLikes -> {
                    sb.append("p.id IN (SELECT pl.post.id FROM Reaction pl WHERE pl.isLike = true GROUP BY pl.post.id HAVING COUNT(pl.id) >= :minLikes) ");
                    sb.append("AND ");
                });

                filterOptions.getMaxLikes().ifPresent(maxLikes -> {
                    sb.append("p.id IN (SELECT pl.post.id FROM Reaction pl WHERE pl.isLike = true GROUP BY pl.post.id HAVING COUNT(pl.id) <= :maxLikes) ");
                    sb.append("AND ");
                });

                sb.setLength(sb.length() - 4);
            }

            boolean orderByIsPresent = filterOptions.getOrderBy().isPresent();
            if (orderByIsPresent) {
                String orderBy = filterOptions.getOrderBy().get();
                if ("title".equalsIgnoreCase(orderBy)) {
                    sb.append("ORDER BY p.title ");
                } else if ("id".equalsIgnoreCase(orderBy)) {
                    sb.append("ORDER BY p.id ");
                } else if ("likes".equalsIgnoreCase(orderBy)) {
                    sb.append("ORDER BY (SELECT COUNT(pld.id) FROM Reaction pld WHERE pld.post.id = p.id AND pld.isLike = true) ");
                } else if ("comments".equalsIgnoreCase(orderBy)) {
                    sb.append("ORDER BY (SELECT COUNT(c.id) FROM Comment c WHERE c.post.id = p.id) ");
                }
            }

            if (filterOptions.getOrderType().isPresent() && orderByIsPresent) {
                sb.append(filterOptions.getOrderType().get().equalsIgnoreCase("desc") ? "DESC" : "ASC");
            }

            Query<Post> query = session.createQuery(sb.toString(), Post.class);
            filterOptions.getTitle().ifPresent(title -> query.setParameter("title", "%" + title + "%"));
            filterOptions.getContent().ifPresent(content -> query.setParameter("content", "%" + content + "%"));
            filterOptions.getTag().ifPresent(tag -> query.setParameter("tag", "%" + tag + "%"));
            filterOptions.getMinLikes().ifPresent(minLikes -> query.setParameter("minLikes", minLikes));
            filterOptions.getMaxLikes().ifPresent(maxLikes -> query.setParameter("maxLikes", maxLikes));
            filterOptions.getUsername().ifPresent(username -> query.setParameter("username", username));

            return query.list();
        }
    }

    @Override
    public Post getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return post;
        }
    }

    @Override
    public void create(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Long id) {
        Post postToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(postToDelete);
            session.getTransaction().commit();
        }
    }
}
