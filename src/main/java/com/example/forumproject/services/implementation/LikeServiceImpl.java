package com.example.forumproject.services.implementation;

import com.example.forumproject.models.Like;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.contracts.LikesRepository;
import com.example.forumproject.services.contracts.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikesRepository likesRepository;

    @Autowired
    public LikeServiceImpl(LikesRepository likesRepository) {
        this.likesRepository = likesRepository;
    }

    @Override
    public int getLikesByPostId(int post_id) {
        return likesRepository.getLikesByPostId(post_id);
    }

    @Override
    public int getDislikesByPostId(int post_id) {
        return likesRepository.getDislikesByPostId(post_id);
    }

    @Override
    public List<Post> getAllLikedPosts(int user_id) {
        return likesRepository.getAllLikedPosts(user_id);
    }

    @Override
    public List<Post> getAllDislikedPosts(int user_id) {
        return likesRepository.getAllDislikedPosts(user_id);
    }

    @Override
    public boolean save(Post post, User user, boolean like) {

        if (likesRepository.checkIfLikeExists(post.getId(), user.getId())) {
            if (like) {
                likesRepository.delete(new Like(post, user, true));
                return false; // return false to print correct msg in controller
                // option A if you like post 2 times you remove the like
                // option B if you like smth 2 times you get error
                /*throw new DuplicateEntityException("You have already liked this post!");*/
            }

            likesRepository.update(new Like(post, user, false));
            return true;
        }
        if (likesRepository.checkIfDislikeExists(post.getId(), user.getId())) {
            if (!like) {
                likesRepository.delete(new Like(post, user, false));
                return false;
//                throw new DuplicateEntityException("Unfortunately you can dislike a post only once!");
            }
            likesRepository.update(new Like(post, user, true));
            return true;
        }

        likesRepository.create(new Like(post, user, like));
        return true;
    }
}
