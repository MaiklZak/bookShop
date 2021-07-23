package com.example.mybookshopapp.entity;

import com.example.mybookshopapp.entity.security.BookstoreUser;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book_review")
public class BookReview implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_bok_review", sequenceName = "seq_bok_review", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bok_review")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private BookstoreUser user;

    @Column(columnDefinition = "TIMESTAMP(6)")
    private LocalDateTime time;

    @Column(columnDefinition = "TEXT")
    private String text;

    @OneToMany(mappedBy = "bookReview")
    private Set<BookReviewLike> bookReviewLikes = new HashSet<>();

    public String getFormatTime() {
        return time.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    public long getLikes() {
        return getBookReviewLikes()
                .stream()
                .filter(brl -> brl.getValue() == 1)
                .count();
    }

    public long getDisLikes() {
        return getBookReviewLikes()
                .stream()
                .filter(brl -> brl.getValue() == -1)
                .count();
    }

    public BookReview(BookstoreUser user, Book book, String text) {
        this();
        this.user = user;
        this.book = book;
        this.text = text;
    }

    public BookReview() {
        this.time = LocalDateTime.now();
    }

    public Set<BookReviewLike> getBookReviewLikes() {
        return bookReviewLikes;
    }

    public void setBookReviewLikes(Set<BookReviewLike> bookReviewLikes) {
        this.bookReviewLikes = bookReviewLikes;
    }

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "BookReview{" +
                "id=" + id +
                ", book=" + book +
                ", time=" + time +
                ", text='" + text + '\'' +
                '}';
    }
}
