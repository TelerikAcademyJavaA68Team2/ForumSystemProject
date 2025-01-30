package com.example.forumproject.services;

import com.example.forumproject.Helpers;
import com.example.forumproject.exceptions.DuplicateEntityException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.UnauthorizedAccessException;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.commentsRepository.CommentRepository;
import com.example.forumproject.services.commentService.CommentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static com.example.forumproject.Helpers.*;
import static com.example.forumproject.helpers.ValidationHelpers.UNAUTHORIZED_MESSAGE_COMMENT;
import static com.example.forumproject.services.PostServiceTests.POST_NOT_FOUND;
import static com.example.forumproject.services.commentService.CommentServiceImpl.DUPLICATE_COMMENT_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests {

    public static final String LIST_SHOULD_BE_EMPTY = "Expected an empty list but got comments.";
    public static final String EXCEPTION_EXPECTED = "Expected EntityNotFoundException but no exception was thrown.";
    public static final String COMMENT_NOT_FOUND = "Comment not found";
    public static final String EXCEPTION_DOES_NOT_MATCH = "Exception message does not match expected.";
    public static final String NOT_AUTHENTICATED = "User not authenticated";
    public static final String UPDATED_COMMENT_CONTENT = "Updated comment content";

    @Mock
    private CommentRepository commentMockRepository;

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    @InjectMocks
    CommentServiceImpl commentService;

    @Test
    public void getById_Should_ReturnComment_When_MatchExists() {
        //Arrange
        Post post = createMockPost();
        when(commentMockRepository.getById(1L, 1L))
                .thenReturn(createMockComment());
        when(postService.getById(1L)).thenReturn(post);

        //Act
        Comment result = commentService.getById(1L, 1L);

        //Assert
        assertEquals(1, result.getId());
        assertEquals(createMockUser(), result.getAuthor());
        assertEquals(VALID_MOCK_CONTENT, result.getContent());
        assertEquals(post, result.getPost());
    }

    @Test
    public void getAllCommentsByPostIdForMapper_Should_ReturnComments_When_ValidCall() {
        //Arrange
        Comment expectedComment = createMockComment();
        when(commentMockRepository.getAllCommentsByPostIdForMapper(1L))
                .thenReturn(List.of(expectedComment));

        //Act
        List<Comment> result = commentService.getAllCommentsByPostId(1L);

        //Assert
        verify(commentMockRepository, Mockito.times(1))
                .getAllCommentsByPostIdForMapper(1L);

        assertEquals(1, result.size());
        assertEquals(List.of(expectedComment), result);

    }

    @Test
    public void getAllCommentsByPostIdForMapper_Should_ReturnEmptyList_When_NoCommentsExist() {
        // Arrange
        when(commentMockRepository.getAllCommentsByPostIdForMapper(1L))
                .thenReturn(Collections.emptyList());

        // Act
        List<Comment> result = commentService.getAllCommentsByPostId(1L);

        // Assert
        Assertions.assertTrue(result.isEmpty(), LIST_SHOULD_BE_EMPTY);
    }

    @Test
    public void getAllCommentsByPostIdForMapper_Should_ThrowException_When_PostNotFound() {
        // Arrange
        when(commentMockRepository.getAllCommentsByPostIdForMapper(999L))
                .thenThrow(new EntityNotFoundException(POST_NOT_FOUND));

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> commentService.getAllCommentsByPostId(999L), EXCEPTION_EXPECTED);
    }

    @Test
    public void getById_Should_ThrowException_When_PostNotFound() {
        // Arrange
        when(postService.getById(999L))
                .thenThrow(new EntityNotFoundException(POST_NOT_FOUND));

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> commentService.getById(999L, 999L), EXCEPTION_EXPECTED);
    }

    @Test
    public void getById_Should_ThrowException_When_CommentNotFound() {
        // Arrange
        Post mockPost = createMockPost();
        when(commentMockRepository.getById(mockPost.getId(), 999L))
                .thenThrow(new EntityNotFoundException(COMMENT_NOT_FOUND));
        when(postService.getById(1L)).thenReturn(mockPost);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> commentService.getById(mockPost.getId(), 999L), EXCEPTION_EXPECTED);
        assertEquals(COMMENT_NOT_FOUND, exception.getMessage(), EXCEPTION_DOES_NOT_MATCH);
    }

    @Test
    public void getAllComments_Should_ThrowException_When_PostNotFound() {
        // Arrange
        Long invalidPostId = 999L;
        when(postService.getById(invalidPostId))
                .thenThrow(new EntityNotFoundException(POST_NOT_FOUND));

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> commentService.getAllComments(invalidPostId), EXCEPTION_EXPECTED);
    }

    @Test
    public void getAllComments_Should_ReturnComments_When_ValidCall() {
        // Arrange
        Long postId = 1L;

        Post mockPost = createMockPost();
        when(postService.getById(postId)).thenReturn(mockPost);

        Comment expectedComment = createMockComment();
        when(commentMockRepository.getAllComments(postId))
                .thenReturn(List.of(expectedComment));

        // Act
        List<Comment> result = commentService.getAllComments(postId);

        // Assert
        verify(postService, Mockito.times(1))
                .getById(postId);
        verify(commentMockRepository, Mockito.times(1))
                .getAllComments(mockPost.getId());

        assertEquals(1, result.size());
        assertEquals(List.of(expectedComment), result);
    }

    @Test
    public void create_Should_CallRepository_When_ValidComment() {
        // Arrange
        Long postId = 1L;
        Comment mockComment = createMockComment();

        // Act
        commentService.create(postId, mockComment);

        // Assert
        verify(commentMockRepository, Mockito.times(1))
                .create(mockComment);
    }

    @Test
    public void create_ShouldReturnComment_WhenValid() {
        // Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = Helpers.createMockPost();
        Comment mockComment = Helpers.createMockComment();

        when(userService.getAuthenticatedUser()).thenReturn(mockUser);
        when(postService.getById(mockPost.getId())).thenReturn(mockPost);
        doNothing().when(commentMockRepository).create(mockComment);

        // Act
        Comment result = commentService.create(mockPost.getId(), mockComment);

        // Assert
        assertNotNull(result);
        assertEquals(Helpers.VALID_MOCK_CONTENT, result.getContent());
        assertEquals(mockUser, result.getAuthor());
        assertEquals(mockPost, result.getPost());

        verify(commentMockRepository, times(1)).create(mockComment);
    }

    @Test
    public void create_ShouldThrowEntityNotFoundException_WhenPostNotFound() {
        // Arrange
        User mockUser = Helpers.createMockUser();
        Comment mockComment = Helpers.createMockComment();

        when(userService.getAuthenticatedUser()).thenReturn(mockUser);
        when(postService.getById(999L)).thenThrow(new EntityNotFoundException(POST_NOT_FOUND));

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> commentService.create(999L, mockComment));

        assertEquals(POST_NOT_FOUND, thrown.getMessage());

        verify(commentMockRepository, never()).create(any(Comment.class));
    }

    @Test
    public void create_ShouldThrowUnauthorizedAccessException_WhenUserNotAuthenticated() {
        // Arrange
        Post mockPost = Helpers.createMockPost();
        Comment mockComment = Helpers.createMockComment();

        when(userService.getAuthenticatedUser()).thenThrow(new UnauthorizedAccessException(NOT_AUTHENTICATED));

        // Act & Assert
        UnauthorizedAccessException thrown = assertThrows(UnauthorizedAccessException.class,
                () -> commentService.create(mockPost.getId(), mockComment));

        assertEquals(NOT_AUTHENTICATED, thrown.getMessage());

        verify(commentMockRepository, never()).create(any(Comment.class));
    }

    @Test
    public void update_ShouldUpdateComment_WhenValid() {
        // Arrange
        User mockUser = Helpers.createMockUser();
        Post mockPost = createMockPost();
        Comment mockComment = Helpers.createMockComment();
        Comment updatedComment = new Comment();
        updatedComment.setId(mockComment.getId());
        updatedComment.setContent(UPDATED_COMMENT_CONTENT);

        when(userService.getAuthenticatedUser()).thenReturn(mockUser);
        when(postService.getById(1L)).thenReturn(mockPost);
        when(commentMockRepository.getById(1L, mockComment.getId())).thenReturn(mockComment);
        doNothing().when(commentMockRepository).update(mockComment);

        // Act
        commentService.update(1L, updatedComment);

        // Assert
        assertEquals(UPDATED_COMMENT_CONTENT, mockComment.getContent());
        verify(commentMockRepository, times(1)).update(mockComment);
    }

    @Test
    public void update_ShouldThrowEntityNotFoundException_WhenCommentNotFound() {
        // Arrange
        Comment mockComment = createMockComment();
        Post mockPost = createMockPost();
        mockComment.setId(999L);
        when(userService.getAuthenticatedUser()).thenReturn(createMockUser());
        when(postService.getById(1L)).thenReturn(mockPost);
        when(commentMockRepository.getById(1L, 999L)).thenThrow(new EntityNotFoundException(COMMENT_NOT_FOUND));

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> commentService.update(1L, mockComment));

        assertEquals(COMMENT_NOT_FOUND, thrown.getMessage());
        verify(commentMockRepository, never()).update(any(Comment.class));
    }

    @Test
    public void update_ShouldThrowUnauthorizedAccessException_WhenUserNotAuthorized() {
        // Arrange
        User anotherUser = new User();
        Post mockPost = createMockPost();
        anotherUser.setId(999L);
        Comment mockComment = Helpers.createMockComment();
        Comment updatedComment = new Comment();
        updatedComment.setId(mockComment.getId());
        updatedComment.setContent(UPDATED_COMMENT_CONTENT);

        when(userService.getAuthenticatedUser()).thenReturn(anotherUser);
        when(postService.getById(1L)).thenReturn(mockPost);
        when(commentMockRepository.getById(1L, mockComment.getId())).thenReturn(mockComment);

        // Act & Assert
        UnauthorizedAccessException thrown = assertThrows(UnauthorizedAccessException.class,
                () -> commentService.update(1L, updatedComment));

        assertEquals(UNAUTHORIZED_MESSAGE_COMMENT, thrown.getMessage());
        verify(commentMockRepository, never()).update(any(Comment.class));
    }

    @Test
    public void update_ShouldThrowDuplicateEntityException_WhenCommentIsDuplicate() {
        // Arrange
        User mockUser = createMockUser();
        Post mockPost = createMockPost();
        Comment mockComment = createMockComment();
        Comment duplicateComment = new Comment();
        duplicateComment.setId(mockComment.getId());
        duplicateComment.setContent(Helpers.VALID_MOCK_CONTENT);

        when(userService.getAuthenticatedUser()).thenReturn(mockUser);
        when(postService.getById(1L)).thenReturn(mockPost);
        when(commentMockRepository.getById(1L, mockComment.getId())).thenReturn(mockComment);

        // Act & Assert
        DuplicateEntityException thrown = assertThrows(DuplicateEntityException.class,
                () -> commentService.update(1L, duplicateComment));

        assertEquals(DUPLICATE_COMMENT_MESSAGE, thrown.getMessage());
        verify(commentMockRepository, never()).update(any(Comment.class));
    }

    @Test
    public void delete_ShouldDeleteComment_WhenUserIsAuthorOrAdmin() {
        // Arrange
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();
        Post mockPost = createMockPost();
        mockComment.setAuthor(mockUser);

        when(userService.getAuthenticatedUser()).thenReturn(mockUser);
        when(postService.getById(1L)).thenReturn(mockPost);
        when(commentMockRepository.getById(1L, 1L)).thenReturn(mockComment);

        // Act
        assertDoesNotThrow(() -> commentService.delete(1L, 1L));

        // Assert
        verify(commentMockRepository, times(1)).delete(1L, 1L);
    }

    @Test
    public void delete_ShouldThrowEntityNotFoundException_WhenCommentNotFound() {
        // Arrange

        when(postService.getById(1L)).thenReturn(createMockPost());
        when(commentMockRepository.getById(1L, 999L))
                .thenThrow(new EntityNotFoundException(COMMENT_NOT_FOUND));

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class,
                () -> commentService.delete(1L, 999L));

        assertEquals(COMMENT_NOT_FOUND, thrown.getMessage());
        verify(commentMockRepository, never()).delete(anyLong(), anyLong());
    }

    @Test
    public void delete_ShouldThrowUnauthorizedAccessException_WhenUserNotAuthorOrAdmin() {
        // Arrange
        Comment mockComment = createMockComment();
        User mockUser = createMockUser();
        Post mockPost = createMockPost();
        mockUser.setId(2L);

        when(postService.getById(1L)).thenReturn(mockPost);
        when(userService.getAuthenticatedUser()).thenReturn(mockUser);
        when(commentMockRepository.getById(1L, 1L)).thenReturn(mockComment);

        // Act & Assert
        UnauthorizedAccessException thrown = assertThrows(UnauthorizedAccessException.class,
                () -> commentService.delete(1L, 1L));

        assertEquals(UNAUTHORIZED_MESSAGE_COMMENT, thrown.getMessage());
        verify(commentMockRepository, never()).delete(anyLong(), anyLong());
    }
}