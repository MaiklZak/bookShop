package com.example.MyBookShopApp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Book> mapper = new BeanPropertyRowMapper(Book.class);

    @Autowired
    public BookService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getBookData() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", mapper);
        return new ArrayList<>(books);
    }

    public List<Book> getBookByTitle(String title) {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books WHERE title = ?", mapper, title);
        return new ArrayList<>(books);
    }
}
