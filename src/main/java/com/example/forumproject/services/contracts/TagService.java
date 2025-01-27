package com.example.forumproject.services.contracts;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.Tag;

import java.util.List;

public interface TagService {

/*  probably wont need these
    boolean checkIfPostIsTagged(int post_id, int tag_id);

    Tag getTag(int post_id, int tag_id);
*/

    List<Tag> getTagsByPostId(Long post_id);

    List<Post> getAllPostsByTag(Long tag_id);

    void create(Tag tag);

    void update(Tag tag);

    void delete(Tag tag);
}
