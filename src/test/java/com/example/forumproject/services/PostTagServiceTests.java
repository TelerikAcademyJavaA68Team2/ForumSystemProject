package com.example.forumproject.services;

import com.example.forumproject.Helpers;
import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.PostTag;
import com.example.forumproject.models.Tag;
import com.example.forumproject.models.User;
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
    public void getAllTagsByPostId_ShouldReturnPosts_When_ValidArgs() {
        // Arrange
        Mockito.when(postTagRepository.getTagsByPostId(Mockito.any()))
                .thenReturn(List.of(Helpers.createMockTag()));

        // Act
        List<Tag> result = service.getTagsByPostId(1L);

        // Assert
        Assertions.assertEquals(1L, result.size());
    }

    @Test
    public void getAllTagsByPostId_ShouldThrowExc_When_PostIdNotValid() {
        // Arrange
        Mockito.when(postRepository.getById(Mockito.any()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getTagsByPostId(1L));
    }

    @Test
    public void getAllPostsByTagId_ShouldReturnPosts_When_ValidArgs() {
        // Arrange
        Post post = Helpers.createMockPost();
        Mockito.when(postTagRepository.getAllPostsByTagId(Mockito.any()))
                .thenReturn(List.of(post));

        // Act
        List<Post> result = service.getAllPostsByTagId(1L);

        // Assert
        Assertions.assertEquals(1L, result.size());
    }

    @Test
    public void getAllPostsByTagId_ShouldThrowExc_When_TagIdNotValid() {
        // Arrange
        Mockito.when(tagRepository.getTagById(Mockito.any()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getAllPostsByTagId(1L));
    }

    @Test
    public void checkIfPostIsTagged_ShouldReturnTrue_When_Valid() {
        // Arrange
        Post post = Helpers.createMockPost();
        Mockito.when(postTagRepository.checkIfPostIsTagged(Mockito.any(), Mockito.any()))
                .thenReturn(true);

        // Act, Assert
        Assertions.assertTrue(service.checkIfPostIsTagged(1L, 1L));
    }

    @Test
    public void checkIfPostIsTagged_ShouldReturnFalse_When_Valid() {
        // Arrange
        Post post = Helpers.createMockPost();
        Mockito.when(postTagRepository.checkIfPostIsTagged(Mockito.any(), Mockito.any()))
                .thenReturn(false);

        // Act, Assert
        Assertions.assertFalse(service.checkIfPostIsTagged(1L, 1L));
    }

    @Test
    public void createTagOnPost_ShouldThrowExc_When_PostId_Invalid() {
        // Arrange
        Mockito.when(postRepository.getById(Mockito.any()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.createTagOnPost(1L, "Test"));
    }

    @Test
    public void createTagOnPost_ShouldThrowExc_When_UserNotAdminOrPostOwner() {
        // Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();
        mockUser.setId(100L);
        Mockito.when(postRepository.getById(Mockito.any()))
                .thenReturn(mockPost);
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(mockUser);

        // Act, Assert
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> service.createTagOnPost(Mockito.any(), "Test"));
    }

    @Test
    public void createTagOnPost_ShouldThrowExc_When_PostAlreadyTagged() {
        // Arrange
        User mockUser = Helpers.createMockAdmin();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();

        mockPost.setAuthor(mockUser);
        Mockito.when(postRepository.getById(Mockito.any()))
                .thenReturn(mockPost);
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(mockUser);
        Mockito.when(tagRepository.getTagByName(Mockito.any())).thenReturn(mockTag);
        Mockito.when(postTagRepository.checkIfPostIsTagged(Mockito.any(), Mockito.any()))
                .thenReturn(true);

        // Act, Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.createTagOnPost(Mockito.any(), "Test"));
    }

    @Test
    public void createTagOnPost_ShouldNotThrowExc_When_ValidArgs() {
        // Arrange
        User mockUser = Helpers.createMockAdmin();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();
        mockPost.setAuthor(mockUser);
        mockUser.setAdmin(true);

        Mockito.when(postRepository.getById(Mockito.any()))
                .thenReturn(mockPost);
        Mockito.when(userService.getAuthenticatedUser())
                .thenReturn(mockUser);
        Mockito.when(tagRepository.getTagByName(mockTag.getTagName()))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(postTagRepository.checkIfPostIsTagged(mockPost.getId(), null))
                .thenReturn(false);

        // Act, Assert
        Assertions.assertDoesNotThrow(() -> service.createTagOnPost(mockPost.getId(), mockTag.getTagName()));
    }

    @Test
    public void updateTagOnPost_ShouldNotThrowExc_When_ValidArgs() {
        // Arrange
        User mockUser = Helpers.createMockAdmin();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();
        PostTag mockPostTag = Helpers.createMockPostTag();
        mockPost.setAuthor(mockUser);
        mockUser.setAdmin(true);

        Mockito.when(postRepository.getById(Mockito.any()))
                .thenReturn(mockPost);
        Mockito.when(userService.getAuthenticatedUser())
                .thenReturn(mockUser);
        Mockito.when(tagRepository.getTagById(1L))
                .thenReturn(mockTag);
        Mockito.when(postTagRepository.getPostTag(mockPost.getId(), mockTag.getId()))
                .thenReturn(mockPostTag);
        Mockito.when(tagRepository.getTagByName("New Tag"))
                .thenThrow(EntityNotFoundException.class);
        Mockito.when(postTagRepository.checkIfPostIsTagged(mockPost.getId(), null))
                .thenReturn(false);

        // Act, Assert
        Assertions.assertDoesNotThrow(() -> service.updateTagOnPost(1L, 1L, "New Tag"));
    }

    @Test
    public void updateTagOnPost_ShouldThrowExc_When_PostAlreadyTagged() {
        // Arrange
        User mockUser = Helpers.createMockAdmin();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();
        Tag mockNewTag = Helpers.createMockTag();
        mockNewTag.setTagName("New Tag");
        mockNewTag.setId(2L);
        PostTag mockPostTag = Helpers.createMockPostTag();
        mockPost.setAuthor(mockUser);
        mockUser.setAdmin(true);

        Mockito.when(postRepository.getById(Mockito.any()))
                .thenReturn(mockPost);
        Mockito.when(userService.getAuthenticatedUser())
                .thenReturn(mockUser);
        Mockito.when(tagRepository.getTagById(1L))
                .thenReturn(mockTag);
        Mockito.when(postTagRepository.getPostTag(mockPost.getId(), mockTag.getId())).thenReturn(mockPostTag);
        Mockito.when(tagRepository.getTagByName("New Tag")).thenReturn(mockNewTag);
        Mockito.when(postTagRepository.checkIfPostIsTagged(mockPost.getId(), 2L))
                .thenReturn(true);

        // Act, Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.updateTagOnPost(1L, 1L, "New Tag"));
    }

    @Test
    public void deleteTagFromPost_ShouldNotThrowExc_When_ValidArgs() {
        // Arrange
        User mockUser = Helpers.createMockAdmin();
        Post mockPost = Helpers.createMockPost();
        Tag mockTag = Helpers.createMockTag();

        Mockito.when(postRepository.getById(Mockito.any()))
                .thenReturn(mockPost);
        Mockito.when(userService.getAuthenticatedUser())
                .thenReturn(mockUser);
        Mockito.when(tagRepository.getTagById(1L))
                .thenReturn(mockTag);
        Mockito.when(postTagRepository.checkIfPostIsTagged(1L, 1L))
                .thenReturn(true);

        // Act, Assert
        Assertions.assertDoesNotThrow(() -> service.deleteTagFromPost(1L, 1L));
    }
}