package com.example.forumproject.services.reactionService;

import com.example.forumproject.models.PostLikesDislikes;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.reactionRepository.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;

    @Autowired
    public ReactionServiceImpl(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    @Override
    public Long getLikesByPostId(Long post_id) {
        return reactionRepository.getLikesByPostId(post_id);
    }

    @Override
    public Long getDislikesByPostId(Long post_id) {
        return reactionRepository.getDislikesByPostId(post_id);
    }

    @Override
    public List<Post> getAllLikedPosts(Long user_id) {
        return reactionRepository.getAllLikedPosts(user_id);
    }

    @Override
    public List<Post> getAllDislikedPosts(Long user_id) {
        return reactionRepository.getAllDislikedPosts(user_id);
    }

    @Override
    public boolean save(Post post, User user, boolean like) {

        if (reactionRepository.checkIfLikeExists(post.getId(), user.getId())) {
            if (like) {
                reactionRepository.delete(new PostLikesDislikes(post, user, true));
                return false; // return false to print correct msg in controller
                // option A if you like post 2 times you remove the like
                // option B if you like smth 2 times you get error
                /*throw new DuplicateEntityException("You have already liked this post!");*/
            }

            reactionRepository.update(new PostLikesDislikes(post, user, false));
            return true;
        }
        if (reactionRepository.checkIfDislikeExists(post.getId(), user.getId())) {
            if (!like) {
                reactionRepository.delete(new PostLikesDislikes(post, user, false));
                return false;
//                throw new DuplicateEntityException("Unfortunately you can dislike a post only once!");
            }
            reactionRepository.update(new PostLikesDislikes(post, user, true));
            return true;
        }

        reactionRepository.create(new PostLikesDislikes(post, user, like));
        return true;
    }
}
