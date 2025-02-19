package com.example.forumproject.mappers;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.postDtos.PostInDto;
import com.example.forumproject.models.dtos.postDtos.PostOutDto;
import com.example.forumproject.models.dtos.postDtos.PostUpdateDto;
import com.example.forumproject.services.commentService.CommentService;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.postTagService.PostTagService;
import com.example.forumproject.services.reactionService.ReactionService;
import com.example.forumproject.services.tagService.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapper {

    private final CommentMapper commentMapper;
    private final PostService postService;
    private final ReactionService reactionService;
    private final CommentService commentService;
    private final PostTagService postTagService;
    private final TagService tagService;

    @Autowired
    public PostMapper(CommentMapper commentMapper, PostService postService,
                      ReactionService reactionService, CommentService commentService, PostTagService postTagService, TagService tagService) {
        this.commentMapper = commentMapper;
        this.postService = postService;
        this.reactionService = reactionService;
        this.commentService = commentService;
        this.postTagService = postTagService;
        this.tagService = tagService;
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

    public PostUpdateDto postToPostUpdateDto(Long id){
        Post post = postService.getById(id);

        PostUpdateDto postUpdateDto = new PostUpdateDto();

        postUpdateDto.setId(post.getId());

        postUpdateDto.setTitle(post.getTitle());

        postUpdateDto.setContent(post.getContent());

        postUpdateDto.setTagNames(postTagService.getTagsByPostId(id)
                .stream()
                .map(Tag::getTagName)
                .toList());

        return postUpdateDto;
    }

    public Post dtoToObject(PostUpdateDto updatePostDTO){
        Post post = postService.getById(updatePostDTO.getId());

        post.setTitle(updatePostDTO.getTitle());

        post.setContent(updatePostDTO.getContent());

        if (updatePostDTO.getTagNames()
                .stream()
                .anyMatch(tag -> !tag.matches("[a-z0-9]+") || tag.length() > 15 || tag.length() < 2)){
            throw new IllegalArgumentException("Tags should be between 2 and 15 symbols (numbers or lowercase letters)!");
        }

        for (int i = 0; i < updatePostDTO.getTagNames().size(); i++) {
            postTagService.createTagOnPost(post.getId(), updatePostDTO.getTagNames().get(i));
        }

        return post;
    }



    public PostOutDto postToPostOutDto(Post post) {
        PostOutDto postOutDto = new PostOutDto();
        postOutDto.setPost_id(post.getId());
        postOutDto.setAuthor(post.getAuthor().getUsername());
        postOutDto.setAuthorId(post.getAuthor().getId());
        postOutDto.setProfilePicture(post.getAuthor().getPhoto());

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
