package com.example.forumproject.services;

import com.example.forumproject.Helpers;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.Reaction;
import com.example.forumproject.models.Tag;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.reactionRepository.ReactionRepository;
import com.example.forumproject.services.reactionService.ReactionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReactionServiceTests {

    @Mock
    private ReactionRepository repository;

    @InjectMocks
    ReactionServiceImpl service;

    @Test
    public void getLikesByPostId_ShouldReturn0L_When_NoLikesFound() {
        // Arrange
        Tag tag = Helpers.createMockTag();
        Mockito.when(repository.getLikesByPostId(tag.getId())).thenReturn(0L);

        // Act
        Long result = service.getLikesByPostId(tag.getId());

        // Assert
        Assertions.assertEquals(0L, result);
    }

    @Test
    public void checkIfLikeExists_returnsTrue_whenValid() {
        // Arrange
       Mockito.when(repository.checkIfLikeExists(Mockito.any(),Mockito.any())).thenReturn(true);

        // Assert
        Assertions.assertTrue(service.checkIfLikeExists(1L,2L));
    }

    @Test
    public void checkIfDislikeExists_returnsTrue_whenValid() {
        // Arrange
       Mockito.when(repository.checkIfDislikeExists(Mockito.any(),Mockito.any())).thenReturn(true);

        // Assert
        Assertions.assertTrue(service.checkIfDislikeExists(1L,2L));
    }


    @Test
    public void getLikesByPostId_ShouldReturnLong_When_ValidArgs() {
        // Arrange
        Tag tag = Helpers.createMockTag();
        Mockito.when(repository.getLikesByPostId(tag.getId())).thenReturn(10L);

        // Act
        Long result = service.getLikesByPostId(tag.getId());

        // Assert
        Assertions.assertEquals(10L, result);
    }

    @Test
    public void getDislikesByPostId_ShouldReturn0L_When_NoDislikesLikesFound() {
        // Arrange
        Tag tag = Helpers.createMockTag();
        Mockito.when(repository.getDislikesByPostId(tag.getId())).thenReturn(0L);

        // Act
        Long result = service.getDislikesByPostId(tag.getId());

        // Assert
        Assertions.assertEquals(0L, result);
    }

    @Test
    public void getDislikesByPostId_ShouldReturnLong_When_ValidArgs() {
        // Arrange
        Tag tag = Helpers.createMockTag();
        Mockito.when(repository.getDislikesByPostId(tag.getId())).thenReturn(10L);

        // Act
        Long result = service.getDislikesByPostId(tag.getId());

        // Assert
        Assertions.assertEquals(10L, result);
    }

    @Test
    public void save_ShouldReturnTrue_When_BooleanLikeIsTrue_And_ValidArgs() {
        // Arrange
        Post post = Helpers.createMockPost();
        User user = Helpers.createMockUser();
        Mockito.when(repository.checkIfLikeExists(post.getId(), user.getId())).thenReturn(false);
        Mockito.when(repository.checkIfDislikeExists(post.getId(), user.getId())).thenReturn(false);

        // Act, Assert
        Assertions.assertTrue(service.save(post, user, true));
    }

    @Test
    public void save_ShouldRemoveLike_When_PostIsLikedAlready() {
        // Arrange
        Post post = Helpers.createMockPost();
        User user = Helpers.createMockUser();
        Mockito.when(repository.checkIfLikeExists(post.getId(), user.getId())).thenReturn(true);

        // Act, Assert
        Assertions.assertFalse(service.save(post, user, true));
    }

    @Test
    public void save_RemovesLike_AndAddDislike_When_PostIsLiked() {
        // Arrange
        Post post = Helpers.createMockPost();
        User user = Helpers.createMockUser();
        Mockito.when(repository.checkIfLikeExists(post.getId(), user.getId())).thenReturn(true);

        // Act, Assert
        Assertions.assertTrue(service.save(post, user, false));
    }

    @Test
    public void save_RemovesDislike_AndAddLike_When_PostIsDisliked() {
        // Arrange
        Post post = Helpers.createMockPost();
        User user = Helpers.createMockUser();
        Mockito.when(repository.checkIfLikeExists(post.getId(), user.getId())).thenReturn(false);
        Mockito.when(repository.checkIfDislikeExists(post.getId(), user.getId())).thenReturn(true);

        // Act, Assert
        Assertions.assertTrue(service.save(post, user, true));
    }

    @Test
    public void save_RemovesDislike_resultingInFalseToReturn_When_PostIsDisliked() {
        // Arrange
        Post post = Helpers.createMockPost();
        User user = Helpers.createMockUser();
        Mockito.when(repository.checkIfLikeExists(post.getId(), user.getId())).thenReturn(false);
        Mockito.when(repository.checkIfDislikeExists(post.getId(), user.getId())).thenReturn(true);

        // Act, Assert
        Assertions.assertFalse(service.save(post, user, false));
    }


}