package com.example.forumproject.services.likeDislikeService;

import com.example.forumproject.models.PostLikesDislikes;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.likesDislikeRepo.LikeDislikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeDislikeRepository likeDislikeRepository;

    @Autowired
    public LikeServiceImpl(LikeDislikeRepository likeDislikeRepository) {
        this.likeDislikeRepository = likeDislikeRepository;
    }

    @Override
    public Long getLikesByPostId(Long post_id) {
        return likeDislikeRepository.getLikesByPostId(post_id);
    }

    @Override
    public Long getDislikesByPostId(Long post_id) {
        return likeDislikeRepository.getDislikesByPostId(post_id);
    }

    @Override
    public List<Post> getAllLikedPosts(Long user_id) {
        return likeDislikeRepository.getAllLikedPosts(user_id);
    }

    @Override
    public List<Post> getAllDislikedPosts(Long user_id) {
        return likeDislikeRepository.getAllDislikedPosts(user_id);
    }

    @Override
    public boolean save(Post post, User user, boolean like) {

        if (likeDislikeRepository.checkIfLikeExists(post.getId(), user.getId())) {
            if (like) {
                likeDislikeRepository.delete(new PostLikesDislikes(post, user, true));
                return false; // return false to print correct msg in controller
                // option A if you like post 2 times you remove the like
                // option B if you like smth 2 times you get error
                /*throw new DuplicateEntityException("You have already liked this post!");*/
            }

            likeDislikeRepository.update(new PostLikesDislikes(post, user, false));
            return true;
        }
        if (likeDislikeRepository.checkIfDislikeExists(post.getId(), user.getId())) {
            if (!like) {
                likeDislikeRepository.delete(new PostLikesDislikes(post, user, false));
                return false;
//                throw new DuplicateEntityException("Unfortunately you can dislike a post only once!");
            }
            likeDislikeRepository.update(new PostLikesDislikes(post, user, true));
            return true;
        }

        likeDislikeRepository.create(new PostLikesDislikes(post, user, like));
        return true;
    }
}
