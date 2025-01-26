package com.example.forumproject.mappers;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.CreatePostDto;
import com.example.forumproject.models.dtos.PostOutDto;
import com.example.forumproject.models.dtos.UpdatePostDto;
import com.example.forumproject.repositories.contracts.CommentRepository;
import com.example.forumproject.repositories.contracts.LikesRepository;
import com.example.forumproject.repositories.contracts.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public PostMapper(PostRepository postRepository, LikesRepository likesRepository, CommentRepository commentRepository, CommentMapper commentMapper) {
        this.postRepository = postRepository;
        this.likesRepository = likesRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public Post createPostFromDto(CreatePostDto createPostDTO, User author){
        Post post = new Post();
        post.setAuthor(author);
        post.setTitle(createPostDTO.getTitle());
        post.setContent(createPostDTO.getContent());
        return post;
    }

    public Post UpdatePostFromDto(UpdatePostDto updatePostDTO, int id){
        Post postToBeUpdate = postRepository.getById(id);
        postToBeUpdate.setTitle(updatePostDTO.getTitle());
        postToBeUpdate.setContent(updatePostDTO.getContent());
        return postToBeUpdate;
    }


    public PostOutDto postOutDtoToPost(Post post) {
        PostOutDto postOutDto = new PostOutDto();
        postOutDto.setId(post.getId());
        postOutDto.setAuthorName(post.getAuthor().getUsername());
        postOutDto.setTitle(post.getTitle());
        postOutDto.setContent(post.getContent());
        postOutDto.setLikes(likesRepository.getLikesByPostId(post.getId()));
        postOutDto.setDislikes(likesRepository.getDislikesByPostId(post.getId()));
        postOutDto.setComments(commentMapper.commentsToCommentDtos(commentRepository.getAllCommentsByPostId(post.getId())));
        return postOutDto;
    }
}
