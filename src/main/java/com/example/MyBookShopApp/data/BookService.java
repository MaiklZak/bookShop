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
        List<Book> books = jdbcTemplate.query("SELECT * FROM books b INNER JOIN authors a ON a.id = b.author_id", mapper);
        return new ArrayList<>(books);
    }

    public List<Book> getBookByAuthor(Integer id) {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books b INNER JOIN authors a ON a.id = b.author_id WHERE a.id = ?", mapper, id);
        return new ArrayList<>(books);
    }
}
