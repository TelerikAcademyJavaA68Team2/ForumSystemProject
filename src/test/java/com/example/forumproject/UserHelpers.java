package com.example.forumproject;

import com.example.forumproject.models.User;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.models.filterOptions.UsersFilterOptions;

public class UserHelpers {
    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName("MockFirstNameUser");
        mockUser.setLastName("MockLastNameUser");
        mockUser.setEmail("mock@user.com");
        mockUser.setUsername("MockUserName");
        mockUser.setPassword("MockPassword");
        mockUser.setAdmin(false);
        return mockUser;
    }

    public static User createMockAdmin() {
        var mockUser = new User();
        mockUser.setId(2L);
        mockUser.setFirstName("MockFirstNameAdmin");
        mockUser.setLastName("MockLastNameAdmin");
        mockUser.setEmail("mock@admin.com");
        mockUser.setUsername("MockAdminName");
        mockUser.setPassword("MockPassword");
        mockUser.setAdmin(true);
        return mockUser;
    }

    public static UsersFilterOptions mockUserFilterOptions() {
        return new UsersFilterOptions(
                "Mock",
                "Mock",
                "mock@ab",
                1L,
                3L,
                "USER",
                "ACTIVE",
                "name",
                "acs");
    }

    public static PostFilterOptions mockPostFilterOptions() {
        return new PostFilterOptions(
                "Electric",
                "the electric car industry",
                "electric",
                1L,
                3L,
                "likes",
                "desc",
                null
        );
    }
}