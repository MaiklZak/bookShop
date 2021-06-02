package com.example.MyBookShopApp.data.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String hash;

    @Column(name = "reg_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime regTime;

    private Integer balance;

    private String name;

    @OneToMany(mappedBy = "user")
    private Set<BookReview> bookReviews = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<BookReviewLike> bookReviewLikes = new HashSet<>();

    public User() {
        this.regTime = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", hash='" + hash + '\'' +
                ", regTime=" + regTime +
                ", balance=" + balance +
                ", name='" + name + '\'' +
                '}';
    }
}
