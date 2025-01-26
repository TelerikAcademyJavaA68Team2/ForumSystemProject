package com.example.forumproject.services;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;

import java.util.List;

public interface TagService {

/*  probably wont need these
    boolean checkIfPostIsTagged(int post_id, int tag_id);

    Tag getTag(int post_id, int tag_id);
*/

    List<Tag> getTagsByPostId(int post_id);

    List<Post> getAllPostsByTag(int tag_id);

    void create(Tag tag);

    void update(Tag tag);

    void delete(Tag tag);
}
