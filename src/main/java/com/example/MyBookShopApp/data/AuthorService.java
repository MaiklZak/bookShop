package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.model.Author;
import com.example.MyBookShopApp.data.reposirories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
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
        List<Author> authors = authorRepository.findAll();
        return authors.stream().collect(Collectors.groupingBy((Author a) -> {return a.getLastName().substring(0, 1);}));
    }
}
