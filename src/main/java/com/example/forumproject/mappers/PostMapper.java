package com.example.forumproject.mappers;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.postDtos.PostInDto;
import com.example.forumproject.models.dtos.postDtos.PostOutDto;
import com.example.forumproject.models.dtos.postDtos.UpdatePostDto;
import com.example.forumproject.services.commentService.CommentService;
import com.example.forumproject.services.likeDislikeService.LikeService;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.tagService.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    private final CommentMapper commentMapper;
    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final TagMapper tagMapper;
    private final TagService tagService;

    @Autowired
    public PostMapper(CommentMapper commentMapper, PostService postService,
                      LikeService likeService, CommentService commentService, TagMapper tagMapper, TagService tagService) {
        this.commentMapper = commentMapper;
        this.postService = postService;
        this.likeService = likeService;
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
        postOutDto.setPost_id(post.getId());
        postOutDto.setAuthor(post.getAuthor().getUsername());
        postOutDto.setTitle(post.getTitle());
        postOutDto.setContent(post.getContent());
        postOutDto.setLikes(likeService.getLikesByPostId(post.getId()));
        postOutDto.setDislikes(likeService.getDislikesByPostId(post.getId()));
        postOutDto.setComments(commentMapper.commentsToCommentDtos(commentService.getAllCommentsByPostId(post.getId())));
        postOutDto.setTags(tagMapper.tagsToTagNames(tagService.getTagsByPostId(post.getId())));
        return postOutDto;
    }
}
