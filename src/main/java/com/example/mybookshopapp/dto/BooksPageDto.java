package com.example.mybookshopapp.dto;

import java.util.List;

public class BooksPageDto {

    private Integer count;
    private List<BookWithAuthorsDto> books;

    public BooksPageDto(List<BookWithAuthorsDto> books) {
        this.books = books;
        this.count = books.size();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<BookWithAuthorsDto> getBooks() {
        return books;
    }

    public void setBooks(List<BookWithAuthorsDto> books) {
        this.books = books;
    }
}
