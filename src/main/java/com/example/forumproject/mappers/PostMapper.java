package com.example.forumproject.mappers;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.postDtos.PostInDto;
import com.example.forumproject.models.dtos.postDtos.PostOutDto;
import com.example.forumproject.services.commentService.CommentService;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.postTagService.PostTagService;
import com.example.forumproject.services.reactionService.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    private final CommentMapper commentMapper;
    private final PostService postService;
    private final ReactionService reactionService;
    private final CommentService commentService;
    private final PostTagService postTagService;

    @Autowired
    public PostMapper(CommentMapper commentMapper, PostService postService,
                      ReactionService reactionService, CommentService commentService, PostTagService postTagService) {
        this.commentMapper = commentMapper;
        this.postService = postService;
        this.reactionService = reactionService;
        this.commentService = commentService;
        this.postTagService = postTagService;
    }

    public Post createPostFromDto(PostInDto postInDTO, User author) {
        Post post = new Post();
        post.setAuthor(author);
        post.setTitle(postInDTO.getTitle());
        post.setContent(postInDTO.getContent());
        return post;
    }

    public Post postInDtoToPost(PostInDto postInDto, Long id) {
        Post postToBeUpdate = postService.getById(id);
        postToBeUpdate.setTitle(postInDto.getTitle());
        postToBeUpdate.setContent(postInDto.getContent());
        return postToBeUpdate;
    }

    public PostOutDto postToPostOutDto(Post post) {
        PostOutDto postOutDto = new PostOutDto();
        postOutDto.setPost_id(post.getId());
        postOutDto.setAuthor(post.getAuthor().getUsername());
        postOutDto.setTitle(post.getTitle());
        postOutDto.setContent(post.getContent());

        postOutDto.setLikes(reactionService
                .getLikesByPostId(post.getId()));

        postOutDto.setDislikes(reactionService
                .getDislikesByPostId(post.getId()));

        postOutDto.setComments(commentMapper
                .commentsToCommentDtos(commentService
                .getAllCommentsByPostId(post.getId())));

        postOutDto.setTags(postTagService.getTagsByPostId(post.getId())
                .stream()
                .map(Tag::getTagName)
                .toList());

        return postOutDto;
    }
}
