package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.model.Author;
import com.example.MyBookShopApp.data.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Map<String, List<Author>> getAuthorsMap() {
        return authorRepository.findAll().stream()
                .collect(Collectors.groupingBy((Author a) -> {return a.getName().substring(0, 1);}));
    }

    public Author getAuthorById(Integer id) {
        return authorRepository.getOne(id);
    }
}
