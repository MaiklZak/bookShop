package com.example.mybookshopapp.entity;

import com.example.mybookshopapp.entity.security.BookstoreUser;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book2user")
public class BookUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private BookUserType type;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BookstoreUser user;

    public BookUser(BookUserType type, Book book, BookstoreUser user) {
        this();
        this.type = type;
        this.book = book;
        this.user = user;
    }

    public BookUser() {
        this.time = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public BookUserType getType() {
        return type;
    }

    public void setType(BookUserType type) {
        this.type = type;
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
