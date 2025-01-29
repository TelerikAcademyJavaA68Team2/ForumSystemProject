package com.example.forumproject.services;

import com.example.forumproject.Helpers;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.Tag;
import com.example.forumproject.repositories.tagsRepository.TagRepository;
import com.example.forumproject.services.tagService.TagServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

public class TagServiceTests {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    TagServiceImpl service;

    @Test
    public void getTagById_ShouldReturnTag_When_ValidArgs() {
        // Arrange
        Tag tag = Helpers.createMockTag();
        Mockito.when(tagRepository.getTagById(1L)).thenReturn(tag);

        // Act
        Tag resultTag = service.getTagById(1L);

        // Assert
        Assertions.assertEquals(tag.getId(), resultTag.getId());
        Assertions.assertEquals(tag.getTagName(), resultTag.getTagName());
        Assertions.assertEquals(tag.getClass(), resultTag.getClass());
    }

    @Test
    public void getTagById_ShouldThrowExc_When_TagIdInvalid() {
        // Arrange
        Tag tag = Helpers.createMockTag();
        Mockito.when(tagRepository.getTagById(1L)).thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getTagById(1L));
    }

    @Test
    public void getTagByName_ShouldReturnTag_When_ValidArgs() {
        // Arrange
        Tag tag = Helpers.createMockTag();
        Mockito.when(tagRepository.getTagByName(tag.getTagName())).thenReturn(tag);

        // Act
        Tag resultTag = service.getTagByName(tag.getTagName());

        // Assert
        Assertions.assertEquals(tag.getId(), resultTag.getId());
        Assertions.assertEquals(tag.getTagName(), resultTag.getTagName());
        Assertions.assertEquals(tag.getClass(), resultTag.getClass());
    }

    @Test
    public void getTagByName_ShouldThrowExc_When_TagNameInvalid() {
        // Arrange
        Tag tag = Helpers.createMockTag();
        Mockito.when(tagRepository.getTagByName(tag.getTagName())).thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getTagByName(tag.getTagName()));
    }
}