package com.example.mybookshopapp.service;

import com.example.mybookshopapp.entity.Tag;
import com.example.mybookshopapp.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Map<Tag, Integer> getTagsAndCount() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .collect(Collectors.toMap(tag -> tag, tag -> tag.getBooks().size()));
    }

    public Tag getById(Integer id) {
        return tagRepository.getOne(id);
    }

    public Integer getMaxCountTagsByBook() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(tag -> tag.getBooks().size()).max(Integer::compare).orElse(0);
    }
}
