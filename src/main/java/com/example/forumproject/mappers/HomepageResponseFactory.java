package com.example.forumproject.mappers;

import com.example.forumproject.repositories.contracts.PostRepository;
import com.example.forumproject.repositories.contracts.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HomepageResponseFactory {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public HomepageResponseFactory(UserRepository userRepository, PostRepository postRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public String getRegisterInfo() {
        StringBuilder sb = new StringBuilder();

        sb.append("Registration information!").append(System.lineSeparator());
        sb.append("In order to register you need to fill this form (its example only).").append(System.lineSeparator());
        sb.append("And do a POST request to /home/register   (the current URL)").append(System.lineSeparator());
        sb.append("Fill this information in the \"raw\" option in your request body!").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("{").append(System.lineSeparator());
        sb.append("    \"firstName\": \"YOUR FIRST NAME\",").append(System.lineSeparator());
        sb.append("    \"lastName\": \"YOUR LAST NAME\",").append(System.lineSeparator());
        sb.append("    \"email\": \"Valid@email.com\",").append(System.lineSeparator());
        sb.append("    \"username\": \"your username\",").append(System.lineSeparator());
        sb.append("    \"password\": \"your password\"").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());

        return sb.toString();
    }

    public String getLoginInfo() {
        StringBuilder sb = new StringBuilder();

        sb.append("Login information!").append(System.lineSeparator());
        sb.append("In order to Login you need to fill this form (its example only).").append(System.lineSeparator());
        sb.append("And do a POST request to /home/login   (the current URL)").append(System.lineSeparator());
        sb.append("Fill this information in the \"raw\" option in your request body!").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("{").append(System.lineSeparator());
        sb.append("    \"username\": \"your username\",").append(System.lineSeparator());
        sb.append("    \"password\": \"your password\"").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());

        return sb.toString();
    }

    //todo dummy data
    public String getHomepageInfo() {

        StringBuilder sb = new StringBuilder();

        sb.append("Welcome to the best Car Forum EVER! :").append(System.lineSeparator());
        sb.append(String.format("Active users: %d", userRepository.getNumberOfActiveUsers())).append(System.lineSeparator());
        sb.append(String.format("Posts created: %d", postRepository.getTotalNumberOfPosts())).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Possible Endpoints without registration:").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("/home/posts List the top 10 most commented or most recent posts.").append(System.lineSeparator());
        sb.append("/home/register Register a new user.").append(System.lineSeparator());
        sb.append("/home/login Log in a user.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Possible Endpoints after registration:").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("/posts: Create and view posts.").append(System.lineSeparator());
        sb.append("/posts/{postId}: View, edit, or delete a specific post.").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("/users/profile: Get and update the user’s profile.").append(System.lineSeparator());
        sb.append("/users/posts: Create, edit, view, and delete user's posts.").append(System.lineSeparator());
        sb.append("/users/posts/{postId}/comments: Add comments to posts.").append(System.lineSeparator());
        sb.append("/users/posts/{postId}/like: Like or dislike a post.").append(System.lineSeparator());

        return sb.toString();
    }

    public String getAdminOptionsInfo() {
        StringBuilder sb = new StringBuilder();

        sb.append("Congrats you are Admin which means you now have access to the forum private parts.").append(System.lineSeparator());
        sb.append("Here are all the URLs you can go to and all the actions you can take.").append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("/admin/profile                    -->  Check your profile.").append(System.lineSeparator());
        sb.append("/admin/profile/phone              -->  Add/Update your phone number so other admins can contact you.").append(System.lineSeparator());
        sb.append("/admin/users/{userId}/block       -->  Block user/admin.").append(System.lineSeparator());
        sb.append("/admin/users/{userId}/unblock     -->  Unblock user/admin.").append(System.lineSeparator());
        sb.append("/admin/users/{userId}/make-admin  -->  Give ADMIN access to other user.").append(System.lineSeparator());
        sb.append("/admin/users/{userId}/make-user   -->  Remove ADMIN access from inactive admin.").append(System.lineSeparator());
        sb.append("/admin/posts/{postId}/delete      -->  Delete any harmful posts.").append(System.lineSeparator());
        sb.append("/admin/posts/{postId}/comments/{commentId}/delete  -->  Delete any harmful comments.").append(System.lineSeparator());

        return sb.toString();
    }


    public String getUpdatePhoneInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("In order to update your phone you need to:").append(System.lineSeparator());
        sb.append("Send a post request to the current URL and add your phone number in the body");
        return sb.toString();
    }

    public String getBlockUserInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("In order to block this User you need to:").append(System.lineSeparator());
        sb.append("Send a post request to the current URL.").append(System.lineSeparator());
        sb.append("If you want to unblock the User later you need to:").append(System.lineSeparator());
        sb.append("Change the block to unblock in the URL and dont forget to change the request to post!");
        return sb.toString();
    }

    public String getUnblockUserInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("In order to unblock this User you need to:").append(System.lineSeparator());
        sb.append("Send a post request to the current URL.").append(System.lineSeparator());
        return sb.toString();
    }

    public String getUpdateToAdminInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("In order to promote this User to Admin you need to:").append(System.lineSeparator());
        sb.append("Send a post request to the current URL.").append(System.lineSeparator());
        return sb.toString();
    }

    public String getDemoteToUserInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("In order to demote this Admin to User you need to:").append(System.lineSeparator());
        sb.append("Send a post request to the current URL.").append(System.lineSeparator());
        return sb.toString();

    }


    public String getDeletePostInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("In order to delete this Post you need to:").append(System.lineSeparator());
        sb.append("Send a post request to the current URL.").append(System.lineSeparator());
        return sb.toString();
    }

    public String getDeleteCommentInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("In order to delete this Comment you need to:").append(System.lineSeparator());
        sb.append("Send a post request to the current URL.").append(System.lineSeparator());
        return sb.toString();
    }
}
