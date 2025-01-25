package com.example.forumproject.mappers;


import com.example.forumproject.models.Comment;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.CommentDto;
import com.example.forumproject.models.dtos.CommentDtoOut;
import com.example.forumproject.repositories.CommentRepository;
import com.example.forumproject.repositories.UserRepository;
import com.example.forumproject.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


    public CommentDtoOut objectToDto(Comment comment) {
        CommentDtoOut commentDtoOut = new CommentDtoOut();
        commentDtoOut.setComment_id(comment.getId());
        commentDtoOut.setAuthor(comment.getAuthor().getUsername());
        commentDtoOut.setContent(comment.getContent());
        return commentDtoOut;
    }
}
