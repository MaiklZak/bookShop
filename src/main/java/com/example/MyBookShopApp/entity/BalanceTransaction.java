package com.example.MyBookShopApp.entity;

import com.example.MyBookShopApp.security.entity.BookstoreUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Entity
@Table(name = "balance_transaction")
public class BalanceTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private BookstoreUser user;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime time;

    private Integer value;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(columnDefinition = "TEXT")
    private String description;

    public BalanceTransaction(BookstoreUser user, Book book, Integer value, String description) {
        this();
        this.user = user;
        this.book = book;
        this.value = value;
        this.description = description;
    }

    public BalanceTransaction() {
        this.time = LocalDateTime.now();
    }

    public String getFormatTime() {
        return time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BookstoreUser getUser() {
        return user;
    }

    public void setUser(BookstoreUser user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
