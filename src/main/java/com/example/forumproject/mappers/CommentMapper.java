package com.example.forumproject.mappers;


import com.example.forumproject.models.Comment;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.CommentDto;
import com.example.forumproject.models.dtos.CommentDtoOut;
import com.example.forumproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final UserRepository userRepository;

    @Autowired
    public CommentMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Comment dtoToObject(CommentDto commentDto, User user){
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setContent(commentDto.getContent());
        return comment;
    }

    public CommentDto forUpdate(Comment comment){
        CommentDto commentDTO = new CommentDto();
        commentDTO.setContent(comment.getContent());
        return commentDTO;
    }

    public CommentDtoOut objectToDto(Comment comment) {
        CommentDtoOut commentDtoOut = new CommentDtoOut();
        commentDtoOut.setComment_id(comment.getId());
        commentDtoOut.setAuthor(comment.getAuthor().getUsername());
        commentDtoOut.setContent(comment.getContent());
        return commentDtoOut;
    }
}
