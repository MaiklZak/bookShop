package com.example.mybookshopapp.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "book2author")
public class BookAuthor implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_book2author_id", sequenceName = "seq_book2author_id", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_book2author_id")
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    private Integer sortIndex = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }
}
