package com.example.forumproject.mappers;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.CreatePostDto;
import com.example.forumproject.models.dtos.PostOutDto;
import com.example.forumproject.models.dtos.UpdatePostDto;
import com.example.forumproject.services.contracts.CommentService;
import com.example.forumproject.services.contracts.LikeService;
import com.example.forumproject.services.contracts.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    private final CommentMapper commentMapper;
    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;

    @Autowired
    public PostMapper(CommentMapper commentMapper, PostService postService, LikeService likeService, CommentService commentService) {
        this.commentMapper = commentMapper;
        this.postService = postService;
        this.likeService = likeService;
        this.commentService = commentService;
    }

    public Post createPostFromDto(CreatePostDto createPostDTO, User author){
        Post post = new Post();
        post.setAuthor(author);
        post.setTitle(createPostDTO.getTitle());
        post.setContent(createPostDTO.getContent());
        return post;
    }

    public Post UpdatePostFromDto(UpdatePostDto updatePostDTO, int id){
        Post postToBeUpdate = postService.getById(id);
        postToBeUpdate.setTitle(updatePostDTO.getTitle());
        postToBeUpdate.setContent(updatePostDTO.getContent());
        return postToBeUpdate;
    }

    public PostOutDto postToPostOutDto(Post post) {
        PostOutDto postOutDto = new PostOutDto();
        postOutDto.setId(post.getId());
        postOutDto.setAuthor(post.getAuthor().getUsername());
        postOutDto.setTitle(post.getTitle());
        postOutDto.setContent(post.getContent());
        postOutDto.setLikes(likeService.getLikesByPostId(post.getId()));
        postOutDto.setDislikes(likeService.getDislikesByPostId(post.getId()));
        postOutDto.setComments(commentMapper.commentsToCommentDtos(commentService.getAllCommentsByPostId(post.getId())));
        return postOutDto;
    }
}
