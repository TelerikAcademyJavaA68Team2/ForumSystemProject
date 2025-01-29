package com.example.forumproject.services;

import com.example.forumproject.Helpers;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.Tag;
import com.example.forumproject.repositories.PostRepository;
import com.example.forumproject.repositories.postTagRepository.PostTagRepository;
import com.example.forumproject.repositories.tagsRepository.TagRepository;
import com.example.forumproject.services.postTagService.PostTagServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PostTagServiceTests {

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PostTagRepository postTagRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    PostTagServiceImpl service;

    @Test
    public void getTagsByPostId_ShouldReturnTags_When_ValidArgs() {
        // Arrange
        /*Post mockPost = Helpers.createMockPost();*/
/*
        Mockito.when(postRepository.getById(Mockito.any()))
                .thenReturn(mockPost);
*/
        Mockito.when(postTagRepository.getTagsByPostId(Mockito.any()))
                .thenReturn(List.of(Helpers.createMockTag()));

        // Act
        List<Tag> result = service.getTagsByPostId(1L);

        // Assert
        Assertions.assertEquals(1L, result.size());
    }

    @Test
    public void getTagsByPostId_ShouldThrowExc_When_PostIdNotValid() {
        // Arrange
        Mockito.when(postRepository.getById(Mockito.any()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getTagsByPostId(1L));
    }


    @Test
    public void getAllPostsByTagId_ShouldReturnPosts_When_ValidArgs() {
        // Arrange
        var mockTag = Helpers.createMockTag();
        Mockito.when(tagRepository.getTagById(Mockito.any()))
                .thenReturn(mockTag);
        Mockito.when(postTagRepository.getTagsByPostId(Mockito.any()))
                .thenReturn(List.of(Helpers.createMockTag()));

        // Act
        List<Tag> result = service.getTagsByPostId(1L);

        // Assert
        Assertions.assertEquals(1L, result.size());
    }

    @Test
    public void getAllPostsByTagId_ShouldThrowExc_When_TagIdNotValid() {
        // Arrange
        Mockito.when(tagRepository.getTagById(Mockito.any()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getTagsByPostId(1L));
    }


}
