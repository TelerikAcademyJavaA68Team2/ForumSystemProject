package com.example.forumproject.mappers;

import com.example.forumproject.models.Tag;
import com.example.forumproject.models.dtos.tagDtos.TagInDto;
import com.example.forumproject.models.dtos.tagDtos.TagOutDto;
import com.example.forumproject.services.tagService.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    private final TagService tagService;

    @Autowired
    public TagMapper(TagService tagService) {
        this.tagService = tagService;
    }

    public Tag TagInDtoToTag(TagInDto tagDTO) {
        Tag tag = new Tag();
        tag.setTagName(tagDTO.getTagName());
        return tag;
    }

    public Tag TagInDtoToTag(TagInDto tagDTO, Long existingTagId) {
        Tag tagToBeUpdated = tagService.getTag(existingTagId);
        tagToBeUpdated.setTagName(tagDTO.getTagName().toLowerCase(Locale.ROOT));
        return tagToBeUpdated;
    }

    public TagOutDto tagToTagOutDto(Tag tag) {
        TagOutDto tagOutDto = new TagOutDto();
        tagOutDto.setTag(tag.getTagName());
        return tagOutDto;
    }

    public List<TagOutDto> tagsToTagOutDtos(List<Tag> tags) {
        return tags.stream().map(this::tagToTagOutDto).collect(Collectors.toList());
    }

    public String TagToTagName(Tag tag){
        return tag.getTagName();
    }

    public List<String> tagsToTagNames(List<Tag> tags) {
        return tags.stream().map(this::TagToTagName).collect(Collectors.toList());
    }

}
