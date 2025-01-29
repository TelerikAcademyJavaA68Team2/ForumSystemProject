package com.example.forumproject.services.commentService;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.commentDtos.CommentOutDto;
import com.example.forumproject.repositories.commentsRepository.CommentRepository;
import com.example.forumproject.repositories.PostRepository;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.forumproject.helpers.ValidationHelpers.isDuplicateComment;
import static com.example.forumproject.helpers.ValidationHelpers.validateUserIsAdminOrCommentAuthor;

@Service
public class CommentServiceImpl implements CommentService {

    public static final String DUPLICATE_COMMENT_MESSAGE = "The comment already has the same content";
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,UserService userService, PostService postService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
    }

    @Override
    public List<Comment> getAll(Long postId) {
        return commentRepository.getAll(postId);
    }

    @Override
    public List<Comment> getAllCommentsByPostId(Long postId) {
        return commentRepository.getAllCommentsByPostId(postId);
    }


    @Override
    public Comment getById(Long postId, Long id) {
        Post postToGetCommentFrom = postService.getById(postId);
        return commentRepository.getById(postToGetCommentFrom.getId(), id);
    }

    @Override
    public List<Comment> getCommentsByAuthor(Long postId) {
        User author = postService.getById(postId).getAuthor();
        return commentRepository.getCommentsByAuthor(postId, author.getId());
    }

    @Override
    public void create(Long postId, Comment comment) {
        User user = userService.getAuthenticatedUser();

        Post postToAddCommentTo = postService.getById(postId);

        comment.setAuthor(user);

        comment.setPost(postToAddCommentTo);

        commentRepository.create(postId, comment);
    }

    @Override
    public void update(Long postId, Comment newComment) {
        User user = userService.getAuthenticatedUser();

        Comment commentToUpdate = commentRepository.getById(postId, newComment.getId());

        validateUserIsAdminOrCommentAuthor(commentToUpdate, user);

        if (isDuplicateComment(newComment, commentToUpdate)) {
            throw new DuplicateEntityException(DUPLICATE_COMMENT_MESSAGE);
        }

        commentToUpdate.setContent(newComment.getContent());

        commentRepository.update(postId, commentToUpdate);
    }

    @Override
    public void delete(Long postId, Long id) {
        User user = userService.getAuthenticatedUser();

        Comment commentToDelete = commentRepository.getById(postId, id);

        validateUserIsAdminOrCommentAuthor(commentToDelete, user);

        commentRepository.delete(postId, id);
    }


}
