package com.example.forumproject;

import com.example.forumproject.models.User;
import com.example.forumproject.models.filterOptions.PostFilterOptions;
import com.example.forumproject.models.filterOptions.UsersFilterOptions;

public class Helpers {

    public static final String VALID_MOCK_TITLE = "Valid mock title for testing";
    public static final String VALID_MOCK_TAG_NAME = "MockTag";
    public static final String VALID_MOCK_CONTENT = "Valid mock content for testing needs to be a bit longer but that's ok!";
    public static final String MOCK_USER_FIRST_NAME = "MockFirstNameUser";
    public static final String MOCK_ADMIN_FIRST_NAME = "MockFirstNameAdmin";
    public static final String MOCK_USER_LAST_NAME = "MockLastNameUser";
    public static final String MOCK_ADMIN_LAST_NAME = "MockLastNameAdmin";
    public static final String MOCK_USER_USERNAME = "MockUsername";
    public static final String MOCK_ADMIN_USERNAME = "MockAdminName";
    public static final String MOCK_PASSWORD = "MockPassword";
    public static final String MOCK_USER_EMAIL = "mock@user.com";
    public static final String MOCK_ADMIN_EMAIL = "mock@admin.com";
    public static final String ACCOUNT_TYPE_USER = "USER";
    public static final String ACCOUNT_STATUS = "ACTIVE";


    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1L);
        mockUser.setFirstName(MOCK_USER_FIRST_NAME);
        mockUser.setLastName(MOCK_USER_LAST_NAME);
        mockUser.setEmail(MOCK_USER_EMAIL);
        mockUser.setUsername(MOCK_USER_USERNAME);
        mockUser.setPassword(MOCK_PASSWORD);
        mockUser.setAdmin(false);
        return mockUser;
    }

    public static User createMockAdmin() {
        var mockUser = new User();
        mockUser.setId(2L);
        mockUser.setFirstName(MOCK_ADMIN_FIRST_NAME);
        mockUser.setLastName(MOCK_ADMIN_LAST_NAME);
        mockUser.setEmail(MOCK_ADMIN_EMAIL);
        mockUser.setUsername(MOCK_ADMIN_USERNAME);
        mockUser.setPassword(MOCK_PASSWORD);
        mockUser.setAdmin(true);
        return mockUser;
    }

    public static Tag createMockTag() {
        var mockTag = new Tag();
        mockTag.setId(1L);
        mockTag.setTagName(VALID_MOCK_TAG_NAME);
        return mockTag;
    }

    public static Post createMockPost() {
        var mockPost = new Post();
        mockPost.setId(1L);
        mockPost.setAuthor(createMockUser());
        mockPost.setTitle(VALID_MOCK_TITLE);
        mockPost.setContent(VALID_MOCK_CONTENT);
        return mockPost;
    }

    public static UsersFilterOptions mockUserFilterOptions() {
        return new UsersFilterOptions(
                MOCK_USER_FIRST_NAME,
                MOCK_USER_USERNAME,
                MOCK_USER_EMAIL,
                1L,
                3L,
                ACCOUNT_TYPE_USER,
                ACCOUNT_STATUS,
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