package com.example.forumproject.mappers;


import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.CommentDto;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.dtos.CommentInDto;
import com.example.forumproject.services.contracts.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    private final CommentService commentService;

    @Autowired
    public CommentMapper(CommentService commentService) {
        this.commentService = commentService;
    }

    public Comment CommentInDtoToObject(CommentInDto commentDto, User user){
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(commentDto.getContent());
        return comment;
    }

    public Comment updateDtoToObject(CommentInDto commentDTO, int postId, int commentId) {
        Comment comment = commentService.getById(postId, commentId);
        comment.setContent(commentDTO.getContent());
        return comment;
    }


    public CommentDto commentToCommentDto(Comment comment) {
        CommentDto commentDtoOut = new CommentDto();
        commentDtoOut.setAuthor(comment.getAuthor().getUsername());
        commentDtoOut.setContent(comment.getContent());
        return commentDtoOut;
    }

    public List<CommentDto> commentsToCommentDtos(List<Comment> comments) {
        return comments.stream().map(this::commentToCommentDto).collect(Collectors.toList());
    }

}
