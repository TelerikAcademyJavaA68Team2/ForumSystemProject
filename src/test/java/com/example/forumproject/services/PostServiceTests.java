package com.example.forumproject.services;

import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.repositories.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.forumproject.Helpers.*;
import static com.example.forumproject.helpers.ValidationHelpers.UNAUTHORIZED_MESSAGE_POST;
import static com.example.forumproject.services.CommentServiceTests.EXCEPTION_EXPECTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    public static final String POST_NOT_FOUND = "Post not found";

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostFilterOptions filterOptions;

    @InjectMocks
    PostServiceImpl postService;

    @Test
    public void getById_ShouldReturnPost_When_ValidArgs() {
        // Arrange
        Post mockPost = createMockPost();
        when(postRepository.getById(any()))
                .thenReturn(mockPost);

        // Act
        Post result = postService.getById(1L);

        // Assert
        Assertions.assertEquals(mockPost, result);
    }

    @Test
    public void getById_ShouldThrowException_When_PostNotFound() {
        // Arrange
        when(postRepository.getById(99L))
                .thenThrow(new EntityNotFoundException(POST_NOT_FOUND));

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class , () -> {
            postService.getById(99L);
        } , EXCEPTION_EXPECTED);
    }

    @Test
    public void getAllPosts_should_callRepository() {
        // Arrange
        when(postRepository.getAll(filterOptions))
                .thenReturn(new ArrayList<>());

        // Act
        postService.getAll(filterOptions);

        // Assert
        verify(postRepository, Mockito.times(1))
                .getAll(filterOptions);
    }

    @Test
    public void create_Should_CallRepository(){
        User mockAuthor = createMockUser();
        Post mockPost = createMockPost();

        // Act
        postService.create(mockPost,mockAuthor);

        // Assert
        verify(postRepository, Mockito.times(1))
                .create(mockPost);
    }

    @Test
    public void delete_Should_CallRepository(){
        User mockAuthor = createMockUser();
        Post mockPost = createMockPost();

        when(postRepository.getById(Mockito.anyLong()))
                .thenReturn(mockPost);
        // Act
        postService.delete(mockPost.getId(),mockAuthor);

        // Assert
        verify(postRepository, Mockito.times(1))
                .delete(mockPost.getId());
    }

    @Test
    public void delete_Should_ThrowException_When_UserIsNotAuthor(){

        Post mockPost = createMockPost();
        User anotherUser = createMockUser();
        anotherUser.setId(99L);

        when(postRepository.getById(Mockito.anyLong()))
                .thenReturn(mockPost);

        // Act & Assert
        UnauthorizedAccessException thrown = Assertions.assertThrows(UnauthorizedAccessException.class, () -> {
            postService.delete(mockPost.getId(), anotherUser);
        });

        assertEquals(UNAUTHORIZED_MESSAGE_POST, thrown.getMessage());
    }

    @Test
    public void delete_ShouldThrowEntityNotFoundException_WhenPostDoesNotExist() {
        // Arrange
        when(postRepository.getById(999L)).thenThrow(new EntityNotFoundException("Post", 999L));
        User mockUser = createMockUser();

        // Act & Assert
        EntityNotFoundException thrown = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            postService.delete(999L, mockUser);
        });

        Assertions.assertEquals("Post with id 999 not found.", thrown.getMessage());

        verify(postRepository, never()).delete(Mockito.anyLong());
    }

    @Test
    public void getNumberOfPostsByUser_ShouldReturnCorrectCount_WhenUserHasPosts() {
        // Arrange
        Long userId = 1L;
        Long expectedPostCount = 5L;

        when(postRepository.getNumberOfPostsByUser(userId)).thenReturn(expectedPostCount);

        // Act
        Long actualPostCount = postService.getNumberOfPostsByUser(userId);

        // Assert
        Assertions.assertEquals(expectedPostCount, actualPostCount);
        verify(postRepository, Mockito.times(1)).getNumberOfPostsByUser(userId);
    }

    @Test
    public void getNumberOfPostsByUser_ShouldReturnZero_WhenUserHasNoPosts() {
        // Arrange
        Long userId = 99L;
        Long expectedPostCount = 0L;

        when(postRepository.getNumberOfPostsByUser(userId)).thenReturn(expectedPostCount);

        // Act
        Long actualPostCount = postService.getNumberOfPostsByUser(userId);

        // Assert
        Assertions.assertEquals(expectedPostCount, actualPostCount);
        verify(postRepository, Mockito.times(1)).getNumberOfPostsByUser(userId);
    }

    @Test
    public void getTotalNumberOfPosts_ShouldReturnCorrectCount() {
        // Arrange
        Long expectedTotalPosts = 20L;

        when(postRepository.getTotalNumberOfPosts()).thenReturn(expectedTotalPosts);

        // Act
        Long actualTotalPosts = postService.getTotalNumberOfPosts();

        // Assert
        Assertions.assertEquals(expectedTotalPosts, actualTotalPosts);
        verify(postRepository, Mockito.times(1)).getTotalNumberOfPosts();
    }

    @Test
    public void getTotalNumberOfPosts_ShouldReturnZero_WhenNoPostsExist() {
        // Arrange
        Long expectedTotalPosts = 0L;

        when(postRepository.getTotalNumberOfPosts()).thenReturn(expectedTotalPosts);

        // Act
        Long actualTotalPosts = postService.getTotalNumberOfPosts();

        // Assert
        Assertions.assertEquals(expectedTotalPosts, actualTotalPosts);
        verify(postRepository, Mockito.times(1)).getTotalNumberOfPosts();
    }

    @Test
    public void update_ShouldUpdatePost_WhenUserIsAdmin() {
        // Arrange
        Post mockPost = createMockPost();
        User admin = createMockAdmin();
        Post updatedPost = createMockPost();
        updatedPost.setTitle("Admin Updated Title");
        updatedPost.setContent("Admin Updated Content");

        when(postRepository.getById(mockPost.getId())).thenReturn(mockPost);

        // Act
        postService.update(updatedPost, admin);

        // Assert
        Assertions.assertEquals("Admin Updated Title", mockPost.getTitle());
        Assertions.assertEquals("Admin Updated Content", mockPost.getContent());
        verify(postRepository, times(1)).update(mockPost);
    }

    @Test
    public void update_ShouldThrowUnauthorizedAccessException_WhenUserIsNotAuthorOrAdmin() {
        // Arrange
        Post mockPost = createMockPost();
        User anotherUser = createMockUser();
        anotherUser.setId(99L);
        Post updatedPost = createMockPost();
        updatedPost.setTitle("Unauthorized Update");

        when(postRepository.getById(mockPost.getId())).thenReturn(mockPost);

        // Act & Assert
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> {
            postService.update(updatedPost, anotherUser);
        });

        verify(postRepository, never()).update(any(Post.class));
    }

    @Test
    public void update_ShouldThrowDuplicateEntityException_WhenPostIsDuplicate() {
        // Arrange
        Post mockPost = createMockPost();
        User author = mockPost.getAuthor();
        Post duplicatePost = createMockPost();

        when(postRepository.getById(mockPost.getId())).thenReturn(mockPost);

        // Act & Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> {
            postService.update(duplicatePost, author);
        });

        verify(postRepository, never()).update(any(Post.class));
    }

    @Test
    public void update_ShouldThrowEntityNotFoundException_WhenPostDoesNotExist() {
        // Arrange
        Post updatedPost = createMockPost();
        when(postRepository.getById(updatedPost.getId())).thenThrow(new EntityNotFoundException(POST_NOT_FOUND));

        // Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            postService.update(updatedPost, createMockUser());
        });

        verify(postRepository, never()).update(any(Post.class));
    }

    @Test
    public void getAll_ShouldReturnFilteredPosts_WhenValidFilterOptionsAreGiven() {
        // Arrange
        PostFilterOptions filterOptions = new PostFilterOptions(
                "Electric", "Future of electric cars", "electric",
                1L, 10L, "likes", "desc", null);

        List<Post> expectedPosts = List.of(createMockPost());

        when(postRepository.getAll(filterOptions)).thenReturn(expectedPosts);

        // Act
        List<Post> result = postService.getAll(filterOptions);

        // Assert
        Assertions.assertEquals(expectedPosts, result);
        verify(postRepository, times(1)).getAll(filterOptions);
    }

    @Test
    public void getAll_ShouldReturnEmptyList_WhenNoPostsMatchFilter() {
        // Arrange
        PostFilterOptions filterOptions = new PostFilterOptions(
                "NonExistingTitle", "No matching content", "nonexistent",
                100L, 200L, "likes", "desc", null);

        when(postRepository.getAll(filterOptions)).thenReturn(List.of());

        // Act
        List<Post> result = postService.getAll(filterOptions);

        // Assert
        Assertions.assertTrue(result.isEmpty());
        verify(postRepository, times(1)).getAll(filterOptions);
    }

    @Test
    public void getAll_ShouldReturnAllPosts_WhenFilterOptionsAreNull() {
        // Arrange
        List<Post> expectedPosts = List.of(createMockPost(), createMockPost());

        when(postRepository.getAll(null)).thenReturn(expectedPosts);

        // Act
        List<Post> result = postService.getAll(null);

        // Assert
        Assertions.assertEquals(expectedPosts, result);
        verify(postRepository, times(1)).getAll(null);
    }
}
