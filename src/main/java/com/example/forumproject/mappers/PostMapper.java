package com.example.forumproject.mappers;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.postDtos.PostInDto;
import com.example.forumproject.models.dtos.postDtos.PostOutDto;
import com.example.forumproject.models.dtos.postDtos.UpdatePostDto;
import com.example.forumproject.services.commentService.CommentService;
import com.example.forumproject.services.reactionService.ReactionService;
import com.example.forumproject.services.postService.PostService;
import com.example.forumproject.services.tagService.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    private final CommentMapper commentMapper;
    private final PostService postService;
    private final ReactionService reactionService;
    private final CommentService commentService;
    private final TagMapper tagMapper;
    private final TagService tagService;

    @Autowired
    public PostMapper(CommentMapper commentMapper, PostService postService,
                      ReactionService reactionService, CommentService commentService, TagMapper tagMapper, TagService tagService) {
        this.commentMapper = commentMapper;
        this.postService = postService;
        this.reactionService = reactionService;
        this.commentService = commentService;
        this.tagMapper = tagMapper;
        this.tagService = tagService;
    }

    public Post createPostFromDto(PostInDto postInDTO, User author){
        Post post = new Post();
        post.setAuthor(author);
        post.setTitle(postInDTO.getTitle());
        post.setContent(postInDTO.getContent());
        return post;
    }

    public Post UpdatePostFromDto(UpdatePostDto updatePostDTO, Long id){
        Post postToBeUpdate = postService.getById(id);
        postToBeUpdate.setTitle(updatePostDTO.getTitle());
        postToBeUpdate.setContent(updatePostDTO.getContent());
        return postToBeUpdate;
    }

    public PostOutDto postToPostOutDto(Post post) {
        PostOutDto postOutDto = new PostOutDto();
        postOutDto.setPostId(post.getId());
        postOutDto.setAuthor(post.getAuthor().getUsername());
        postOutDto.setTitle(post.getTitle());
        postOutDto.setContent(post.getContent());
        postOutDto.setLikes(reactionService.getLikesByPostId(post.getId()));
        postOutDto.setDislikes(reactionService.getDislikesByPostId(post.getId()));
        postOutDto.setComments(commentMapper.commentsToCommentDtos(commentService.getAllCommentsByPostId(post.getId())));
        postOutDto.setTags(tagMapper.tagsToTagNames(tagService.getTagsByPostId(post.getId())));
        return postOutDto;
    }
}
