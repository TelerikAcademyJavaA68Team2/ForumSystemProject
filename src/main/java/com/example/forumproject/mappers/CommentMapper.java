package com.example.forumproject.mappers;


import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.CommentDto;
import com.example.forumproject.models.Comment;
import com.example.forumproject.repositories.contracts.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentMapper(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment dtoToObject(CommentDto commentDto, User user){
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(commentDto.getContent());
        return comment;
    }

    public Comment updateDtoToObject(CommentDto commentDTO, int postId, int commentId) {
        Comment comment = commentRepository.getById(postId, commentId);
        comment.setContent(commentDTO.getContent());
        return comment;
    }


    public CommentDto commentToCommentDto(Comment comment) {
        CommentDto commentDtoOut = new CommentDto();
        commentDtoOut.setContent(comment.getContent());
        return commentDtoOut;
    }

    public List<CommentDto> commentsToCommentDtos(List<Comment> comments) {
        return comments.stream().map(this::commentToCommentDto).collect(Collectors.toList());
    }

}
