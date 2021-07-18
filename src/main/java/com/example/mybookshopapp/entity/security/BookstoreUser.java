package com.example.mybookshopapp.entity.security;

import com.example.mybookshopapp.entity.BookReview;
import com.example.mybookshopapp.entity.BookReviewLike;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class BookstoreUser implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_user", sequenceName = "seq_user", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
    private Integer id;

    private String hash;

    @Column(name = "reg_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime regTime;

    @Column(columnDefinition="INT default '0'")
    private Integer balance = 0;

    private String name;

    @OneToMany(mappedBy = "user")
    private Set<BookReview> bookReviews = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<BookReviewLike> bookReviewLikes = new HashSet<>();

    public BookstoreUser() {
        this.regTime = LocalDateTime.now();
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public LocalDateTime getRegTime() {
        return regTime;
    }

    public void setRegTime(LocalDateTime regTime) {
        this.regTime = regTime;
    }

    public Set<BookReview> getBookReviews() {
        return bookReviews;
    }

    public void setBookReviews(Set<BookReview> bookReviews) {
        this.bookReviews = bookReviews;
    }

    public Set<BookReviewLike> getBookReviewLikes() {
        return bookReviewLikes;
    }

    public void setBookReviewLikes(Set<BookReviewLike> bookReviewLikes) {
        this.bookReviewLikes = bookReviewLikes;
    }
}
