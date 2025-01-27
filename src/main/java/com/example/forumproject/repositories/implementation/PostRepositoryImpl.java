package com.example.forumproject.repositories.implementation;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.repositories.contracts.PostRepository;
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
    public List<Post> getAllPostsFromUser(Long user_id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("FROM Post p WHERE p.author.id = :userId", Post.class);
            query.setParameter("userId", user_id);
            return query.list();
        }
    }

    @Override
    public Long getTotalNumberOfPosts() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(p) FROM Post p", Long.class);
            return query.uniqueResult() != null ? query.uniqueResult() : 0;
        }
    }

    public List<Post> qdqd() {


        try (Session session = sessionFactory.openSession()) {

            Query<Post> posts = session.createQuery("FROM Post p WHERE p.id IN (SELECT pt.post.id FROM PostTag pt JOIN pt.tag t WHERE t.name LIKE :tags) ", Post.class);

            return posts.list();
        }
    }

    public List<Post> getAll(PostFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder hql = new StringBuilder("FROM Post p ");
            boolean hasConditions = false;

            // Build dynamic WHERE clause
            if (filterOptions.getTitle().isPresent() || filterOptions.getContent().isPresent() ||
                    filterOptions.getTags().isPresent() || filterOptions.getMinLikes().isPresent() ||
                    filterOptions.getMaxLikes().isPresent()) {

                hql.append("WHERE ");
                hasConditions = true;

                // Filtering by title
                filterOptions.getTitle().ifPresent(title -> {
                    hql.append("p.title LIKE :title ");
                    hql.append("AND ");
                });

                // Filtering by content
                filterOptions.getContent().ifPresent(content -> {
                    hql.append("p.content LIKE :content ");
                    hql.append("AND ");
                });

                // Filtering by tags
                filterOptions.getTags().ifPresent(tags -> {
                    hql.append("p.id IN (SELECT pt.post.id FROM PostTag pt JOIN pt.tag t WHERE t.name LIKE :tags) ");
                    hql.append("AND ");
                });

                // Filtering by min likes
                filterOptions.getMinLikes().ifPresent(minLikes -> {
                    hql.append("p.id IN (SELECT pl.post.id FROM PostLikesDislikes pl WHERE pl.isLike = 1 GROUP BY pl.post.id HAVING COUNT(pl.id) >= :minLikes) ");
                    hql.append("AND ");
                });

                // Filtering by max likes
                filterOptions.getMaxLikes().ifPresent(maxLikes -> {
                    hql.append("p.id IN (SELECT pl.post.id FROM PostLikesDislikes pl WHERE pl.isLike = 1 GROUP BY pl.post.id HAVING COUNT(pl.id) <= :maxLikes) ");
                    hql.append("AND ");
                });

                // Remove the trailing "AND"
                hql.setLength(hql.length() - 4);
            }

            // Add sorting
          /*  filterOptions.getSortBy().ifPresent(sortBy -> {
                if ("title".equalsIgnoreCase(sortBy)) {
                    hql.append("ORDER BY p.title ");
                } else if ("date".equalsIgnoreCase(sortBy)) {
                    hql.append("ORDER BY p.createdAt ");
                }
            });*/

            // Add order (ascending or descending)
            if (filterOptions.getOrderBy().isPresent()) {
                hql.append(filterOptions.getOrderBy().get().equalsIgnoreCase("desc") ? "DESC" : "ASC");
            }

            Query<Post> query = session.createQuery(hql.toString(), Post.class);

            // Set parameters for dynamic HQL
            filterOptions.getTitle().ifPresent(title -> query.setParameter("title", "%" + title + "%"));
            filterOptions.getContent().ifPresent(content -> query.setParameter("content", "%" + content + "%"));
            filterOptions.getTags().ifPresent(tags -> query.setParameter("tags", "%" + tags + "%"));
            filterOptions.getMinLikes().ifPresent(minLikes -> query.setParameter("minLikes", minLikes));
            filterOptions.getMaxLikes().ifPresent(maxLikes -> query.setParameter("maxLikes", maxLikes));

            return query.list();
        }

       /* try (
                Session session = sessionFactory.openSession()) {


            Query<Post> query = session.createQuery("from Post where title like :title and content like :content", Post.class);
            query.setParameter("title", "pesho");
            query.setParameter("content", "conteeens");
            return query.list();
        }*/
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
