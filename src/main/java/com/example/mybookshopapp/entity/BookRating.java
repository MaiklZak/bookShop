package com.example.mybookshopapp.entity;

import com.example.mybookshopapp.entity.security.BookstoreUser;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "book_rating")
public class BookRating implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_bok_rating", sequenceName = "seq_bok_rating", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bok_rating")
    private Integer id;

    private Integer value;

    @ManyToOne()
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private BookstoreUser user;

    public BookRating(BookstoreUser user, Book book, Integer value) {
        this.user = user;
        this.book = book;
        this.value = value;
    }

    public BookRating() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BookstoreUser getUser() {
        return user;
    }

    public void setUser(BookstoreUser user) {
        this.user = user;
    }
}