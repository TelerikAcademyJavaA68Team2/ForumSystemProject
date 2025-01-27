package com.example.forumproject.mappers;

import com.example.forumproject.models.Tag;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    public String TagToTagName(Tag tag){
        return tag.getTagName();
    }

    public List<String> tagsToTagNames(List<Tag> tags) {
        return tags.stream().map(this::TagToTagName).collect(Collectors.toList());
    }
}
