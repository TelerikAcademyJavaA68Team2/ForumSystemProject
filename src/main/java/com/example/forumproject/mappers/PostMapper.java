package com.example.forumproject.mappers;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.CreatePostDto;
import com.example.forumproject.models.dtos.UpdatePostDto;
import com.example.forumproject.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    private final PostRepository postRepository;

    @Autowired
    public PostMapper(PostRepository postRepository) {
        this.postRepository = postRepository;
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


}
