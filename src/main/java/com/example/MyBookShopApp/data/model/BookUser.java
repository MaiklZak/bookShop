package com.example.MyBookShopApp.data.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "book2user")
public class BookUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date time;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private BookUserType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "book_id")
    private Book book;

    public BookUserType getType() {
        return type;
    }

    public void setType(BookUserType type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
