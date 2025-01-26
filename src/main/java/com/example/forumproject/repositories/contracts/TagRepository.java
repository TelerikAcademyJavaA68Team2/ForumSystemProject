package com.example.forumproject.repositories.contracts;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;

import java.util.List;

public interface TagRepository {

    boolean checkIfPostIsTagged(int post_id, int tag_id);

    Tag getTag(int post_id, int tag_id);

    List<Tag> getTagsByPostId(int post_id);

    List<Post> getAllPostsByTag(int tag_id);

    void create(Tag tag);

    void update(Tag tag);

    void delete(Tag tag);
}
