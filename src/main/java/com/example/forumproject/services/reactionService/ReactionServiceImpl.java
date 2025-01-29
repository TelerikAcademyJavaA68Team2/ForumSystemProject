package com.example.forumproject.services.reactionService;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.Reaction;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.reactionRepository.ReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean save(Post post, User user, boolean like) {

        if (reactionRepository.checkIfLikeExists(post.getId(), user.getId())) {
            if (like) {
                reactionRepository.delete(new Reaction(post, user, true));
                return false;
            }

            reactionRepository.update(new Reaction(post, user, false));
            return true;
        }

        if (reactionRepository.checkIfDislikeExists(post.getId(), user.getId())) {
            if (!like) {
                reactionRepository.delete(new Reaction(post, user, false));
                return false;
            }
            reactionRepository.update(new Reaction(post, user, true));
            return true;
        }

        reactionRepository.create(new Reaction(post, user, like));
        return true;
    }
}