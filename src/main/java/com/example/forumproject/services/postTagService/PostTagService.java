package com.example.forumproject.services.postTagService;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;

public interface PostTagService {

    void addTagToPost(Post post, Tag tag);

}
