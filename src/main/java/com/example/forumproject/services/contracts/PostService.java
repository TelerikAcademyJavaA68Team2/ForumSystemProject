package com.example.forumproject.services.contracts;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.dtos.PostOutDto;

import java.util.List;

public interface PostService {

   // List<Post> get(FilterOptions filterOptions);

    List<Post> getAll();

    PostOutDto getByIdDto(int id);

    Post getById(int id);

    void create(Post beer, User user);

    void update(Post beer, User user);

    void delete(int id, User user);

}
