package com.example.MyBookShopApp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Author> mapper = new BeanPropertyRowMapper(Author.class);

    @Autowired
    public AuthorService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Author> getAuthors() {
        List<Author> authors = jdbcTemplate.query("SELECT * FROM authors", mapper);
        return new ArrayList<>(authors);
    }

    public Author get(Integer id) {
        List<Author> authors = jdbcTemplate.query("SELECT * FROM authors WHERE id = ?", mapper, id);
        return authors.stream().findAny().orElse(null);
    }
}
