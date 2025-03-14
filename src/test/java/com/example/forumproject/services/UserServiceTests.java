package com.example.forumproject.services;

import com.example.forumproject.Helpers;
import com.example.forumproject.exceptions.*;
import com.example.forumproject.models.User;
import com.example.forumproject.models.filterOptions.UsersFilterOptions;
import com.example.forumproject.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    UserRepository mockRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    UserServiceImpl service;

    @Test
    public void getAllUsers_ShouldReturnUserList_When_ValidArgs() {
        // Arrange
        UsersFilterOptions filterOptions = Helpers.mockUserFilterOptions();
        Mockito.when(mockRepository.getAllUsers(filterOptions))
                .thenReturn(List.of(Helpers.createMockUser()));

        // Act
        List<User> result = service.getAllUsers(filterOptions);
        int size = result.size();
        // Assert
        Assertions.assertEquals(1L, size);
    }

    @Test
    public void getNumberOfRegisteredUsers_ShouldReturnLong_When_NoArgs() {
        // Arrange
        Mockito.when(mockRepository.getNumberOfRegisteredUsers())
                .thenReturn(10L);
        /*Long number = service.getNumberOfRegisteredUsers();*/

        // Act, Assert
        Assertions.assertEquals(10L, service.getNumberOfRegisteredUsers());
    }

    @Test
    public void loadUserByUsername_ShouldReturnUser_When_ValidArgs() {
        // Arrange
        Mockito.when(mockRepository.getByUsername(Mockito.any()))
                .thenReturn(Helpers.createMockUser());

        // Act, Assert
        Assertions.assertEquals(1L, service.loadUserByUsername(Mockito.any()).getId());
    }

    @Test
    public void getByEmail_ShouldReturnUser_When_ValidArgs() {
        // Arrange
        Mockito.when(mockRepository.getByEmail("mock@user.com")).thenReturn(Helpers.createMockUser());

        // Act
        User result = service.getByEmail("mock@user.com");

        // Assert
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(Helpers.MOCK_USER_FIRST_NAME, result.getFirstName());
        Assertions.assertEquals(Helpers.MOCK_USER_LAST_NAME, result.getLastName());
        Assertions.assertEquals(Helpers.MOCK_USER_EMAIL, result.getEmail());
        Assertions.assertEquals(Helpers.MOCK_USER_USERNAME, result.getUsername());
        Assertions.assertEquals(Helpers.MOCK_PASSWORD, result.getPassword());
        Assertions.assertNull(result.getPhoto());
        Assertions.assertEquals("[USER]", result.getAuthorities().toString());
    }

    @Test
    public void getById_ShouldReturnUser_When_ValidArgs() {
        // Arrange
        Mockito.when(mockRepository.getById(1L)).thenReturn(Helpers.createMockUser());

        // Act
        User result = service.getById(1L);

        // Assert
        Assertions.assertEquals(1, result.getId());
    }

    @Test
    public void deleteUser_ShouldDeleteUser_When_ValidArgs() {
        // Arrange
        User user = Helpers.createMockUser();
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);

        // Act, Assert
        Assertions.assertDoesNotThrow(() -> service.deleteUser());
    }

    @Test
    public void update_ShouldUpdateUser_When_ValidArgs() {
        // Arrange
        User user = Helpers.createMockUser();

        // Act, Assert
        Assertions.assertDoesNotThrow(() -> service.update(user));
    }

    @Test
    public void save_ShouldThrowExc_When_emailExists() {
        // Arrange
        User user = Helpers.createMockUser();
        Mockito.when(mockRepository.getByUsername(Mockito.any()))
                .thenThrow(UsernameNotFoundException.class);
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenReturn(user);

        // Act, Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.save(user));
    }

    @Test
    public void save_ShouldThrowExc_When_usernameTaken() {
        // Arrange
        User user = Helpers.createMockUser();
        Mockito.when(mockRepository.getByUsername(Mockito.any()))
                .thenReturn(user);

        // Act, Assert
        Assertions.assertThrows(DuplicateEntityException.class, () -> service.save(user));
    }

    @Test
    public void save_ShouldThrowExc_When_emailInvalid() {
        // Arrange
        User user = Helpers.createMockUser();
        user.setEmail("£££££££££");
        Mockito.when(mockRepository.getByUsername(Mockito.any()))
                .thenThrow(UsernameNotFoundException.class);
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);
        // Act, Assert
        Assertions.assertThrows(InvalidEmailFormatException.class, () -> service.save(user));
    }

    @Test
    public void save_ShouldNotThrowExc_When_ArgsValid() {
        // Arrange
        User user = Helpers.createMockUser();
        Mockito.when(mockRepository.getByUsername(Mockito.any()))
                .thenThrow(UsernameNotFoundException.class);
        Mockito.when(mockRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertDoesNotThrow(() -> service.save(user));
    }

    @Test
    public void promoteToAdmin_ShouldThrowExc_When_IdInvalid() {
        // Arrange
        Mockito.when(mockRepository.getById(Mockito.any()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.promoteToAdmin(1L));
    }

    @Test
    public void promoteToAdmin_ShouldThrowExc_When_UserIsAdmin() {
        // Arrange
        User user = Helpers.createMockAdmin();
        Mockito.when(mockRepository.getById(Mockito.any()))
                .thenReturn(user);

        // Act, Assert
        Assertions.assertThrows(InvalidUserInputException.class, () -> service.promoteToAdmin(1L));
    }

    @Test
    public void promoteToAdmin_ShouldNotThrowExc_When_ValidArgs() {
        // Arrange
        User user = Helpers.createMockUser();
        Mockito.when(mockRepository.getById(Mockito.any()))
                .thenReturn(user);

        // Act, Assert
        Assertions.assertDoesNotThrow(() -> service.promoteToAdmin(2L));
    }

    @Test
    public void demoteAdminToUser_ShouldThrowExc_When_IdInvalid() {
        // Arrange
        Mockito.when(mockRepository.getById(Mockito.any()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.demoteAdminToUser(Mockito.any()));
    }

    @Test
    public void demoteAdminToUser_ShouldThrowExc_When_UserIsAdmin() {
        // Arrange
        User user = Helpers.createMockUser();
        Mockito.when(mockRepository.getById(Mockito.any()))
                .thenReturn(user);

        // Act, Assert
        Assertions.assertThrows(InvalidUserInputException.class, () -> service.demoteAdminToUser(1L));
    }

    @Test
    public void demoteAdminToUser_ShouldNotThrowExc_When_ValidArgs() {
        // Arrange
        User user = Helpers.createMockAdmin();
        Mockito.when(mockRepository.getById(Mockito.any()))
                .thenReturn(user);

        // Act, Assert
        Assertions.assertDoesNotThrow(() -> service.demoteAdminToUser(2L));
    }

    @Test
    public void blockUser_ShouldThrowExc_When_IdInvalid() {
        // Arrange
        User user = Helpers.createMockUser();
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        Mockito.when(mockRepository.getByUsername(Mockito.any())).thenReturn(user);

        Mockito.when(mockRepository.getById(Mockito.anyLong()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.blockUser(999L));
    }

    @Test
    public void blockUser_ShouldThrowExc_When_UserIsBlocked() {
        // Arrange
        User admin = Helpers.createMockAdmin();
        admin.setBlocked(false);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(admin);
        Mockito.when(mockRepository.getByUsername(Mockito.any())).thenReturn(admin);

        User user = Helpers.createMockUser();
        user.setBlocked(true);
        Mockito.when(mockRepository.getById(1L)).thenReturn(user);

        // Act, Assert
        Assertions.assertThrows(InvalidUserInputException.class, () -> service.blockUser(1L));
    }

    @Test
    public void blockUser_ShouldNotThrowExc_When_ValidArgs() {
        // Arrange
        User admin = Helpers.createMockAdmin();
        admin.setBlocked(false);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(admin);
        Mockito.when(mockRepository.getByUsername(Mockito.any())).thenReturn(admin);


        User user = Helpers.createMockUser();
        user.setBlocked(false);
        Mockito.when(mockRepository.getById(1L)).thenReturn(user);

        // Act, Assert
        Assertions.assertDoesNotThrow(() -> service.blockUser(1L));
    }

    @Test
    public void blockUser_ShouldThrowExc_When_BlockingSelf() {
        // Arrange
        User user = Helpers.createMockUser();
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        Mockito.when(mockRepository.getByUsername(Mockito.any())).thenReturn(user);


        // Act, Assert
        Assertions.assertThrows(InvalidUserInputException.class, () -> service.blockUser(user.getId()));
    }

    @Test
    public void unblockUser_ShouldThrowExc_When_IdInvalid() {
        // Arrange
        Mockito.when(mockRepository.getById(Mockito.any()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.unblockUser(Mockito.any()));
    }

    @Test
    public void unblockUser_ShouldThrowExc_When_UserIsBlocked() {
        // Arrange
        User user = Helpers.createMockUser();
        user.setBlocked(false);
        Mockito.when(mockRepository.getById(Mockito.any()))
                .thenReturn(user);

        // Act, Assert
        Assertions.assertThrows(InvalidUserInputException.class, () -> service.unblockUser(1L));
    }

    @Test
    public void unblockUser_ShouldNotThrowExc_When_ValidArgs() {
        // Arrange
        User user = Helpers.createMockAdmin();
        user.setBlocked(true);
        Mockito.when(mockRepository.getById(Mockito.any()))
                .thenReturn(user);

        // Act, Assert
        Assertions.assertDoesNotThrow(() -> service.unblockUser(2L));
    }

    @Test
    void getAuthenticatedUser_ReturnUser_When_ValidToken() {
        // Arrange
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        User user = Helpers.createMockUser();
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        Mockito.when(mockRepository.getByUsername(Mockito.any())).thenReturn(user);

        // Act
        User result = service.getAuthenticatedUser();

        // Assert
        Assertions.assertEquals(user, result);
    }

    @Test
    void getAuthenticatedUser_ShouldNotThrowExc_When_ValidToken() {
        // Arrange
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        User user = Helpers.createMockUser();
        Mockito.when(authentication.getPrincipal()).thenReturn(Helpers.createMockUser());

        // Act, Assert
        Assertions.assertDoesNotThrow(() -> service.getAuthenticatedUser());
    }

    @Test
    void getAuthenticatedUser_ShouldThrowExc_When_NoToken() {
        // Arrange
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        User user = Helpers.createMockUser();
        Mockito.when(authentication.getPrincipal()).thenReturn(null);

        // Act, Assert
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> service.getAuthenticatedUser());
    }
}