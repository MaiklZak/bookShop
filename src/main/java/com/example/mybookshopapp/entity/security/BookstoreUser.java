package com.example.mybookshopapp.entity.security;

import com.example.mybookshopapp.entity.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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

    @Column(name = "reg_time", columnDefinition = "TIMESTAMP(6)")
    private LocalDateTime regTime;

    @Column(columnDefinition="INT default '0'")
    private Integer balance = 0;

    private String name;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UserContact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<BookReview> bookReviews = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<BookReviewLike> bookReviewLikes = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<BookRating> ratings = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<BookUser> bookUsers = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<BalanceTransaction> balanceTransactions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<FileDownload> fileDownloads = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Message> messages = new HashSet<>();

    public BookstoreUser() {
        this.regTime = LocalDateTime.now();
    }

    @JsonProperty("formatTime")
    public String getFormatTime() {
        return regTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    @JsonProperty("roleString")
    public String getRolesString() {
        return roles.toString().replaceAll("[\\[\\]]", "");
    }

    @PreRemove
    private void preRemove() {
        messages.forEach(message -> message.setUser(null));
        bookReviews.forEach(review -> review.setUser(null));
    }

    public Set<UserContact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<UserContact> contacts) {
        this.contacts = contacts;
    }

    public Set<FileDownload> getFileDownloads() {
        return fileDownloads;
    }

    public void setFileDownloads(Set<FileDownload> fileDownloads) {
        this.fileDownloads = fileDownloads;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public Set<BalanceTransaction> getBalanceTransactions() {
        return balanceTransactions;
    }

    public void setBalanceTransactions(Set<BalanceTransaction> balanceTransactions) {
        this.balanceTransactions = balanceTransactions;
    }

    public Set<BookRating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<BookRating> ratings) {
        this.ratings = ratings;
    }

    public Set<BookUser> getBookUsers() {
        return bookUsers;
    }

    public void setBookUsers(Set<BookUser> bookUsers) {
        this.bookUsers = bookUsers;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
