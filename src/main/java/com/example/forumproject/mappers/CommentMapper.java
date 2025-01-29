package com.example.forumproject.mappers;


import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.commentDtos.CommentOutDto;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.dtos.commentDtos.CommentInDto;
import com.example.forumproject.services.commentService.CommentService;
import com.example.forumproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentMapper(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    public Comment CommentInDtoToObject(CommentInDto commentDto) {
        User user = userService.getAuthenticatedUser();
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(commentDto.getContent());
        return comment;
    }

    public Comment updateDtoToObject(CommentInDto commentDTO, Long postId, Long commentId) {
        Comment comment = commentService.getById(postId, commentId);
        comment.setContent(commentDTO.getContent());
        return comment;
    }


    public CommentOutDto commentToCommentOutDto(Comment comment) {
        CommentOutDto commentOutDto = new CommentOutDto();
        commentOutDto.setAuthor(comment.getAuthor().getUsername());
        commentOutDto.setContent(comment.getContent());
        return commentOutDto;
    }

    public List<CommentOutDto> commentsToCommentDtos(List<Comment> comments) {
        return comments.stream().map(this::commentToCommentOutDto).collect(Collectors.toList());
    }

}
