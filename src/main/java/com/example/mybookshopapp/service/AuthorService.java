package com.example.mybookshopapp.service;

import com.example.mybookshopapp.entity.Author;
import com.example.mybookshopapp.entity.Book;
import com.example.mybookshopapp.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Map<String, List<Author>> getAuthorsMap() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream().sorted(Comparator.comparing(Author::getName)).collect(Collectors.groupingBy((Author a) -> a.getName().substring(0, 1)));
    }

    public List<Author> getAuthorsByBook(Book book) {
        return authorRepository.findByBook(book);
    }

    public Author getAuthorBySlug(String slug) {
        return authorRepository.findBySlug(slug);
    }

    public void save(Author author) {
        authorRepository.save(author);
    }
}
