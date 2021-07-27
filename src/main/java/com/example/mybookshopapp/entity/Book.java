package com.example.mybookshopapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "books")
@ApiModel(description = "entity representing a book")
public class Book implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_book_id", sequenceName = "seq_book_id", initialValue = 1001, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_book_id")
    private Integer id;

    @Column(name = "pub_date")
    @ApiModelProperty("date of book publication")
    private Date pubDate;

    @Column(name = "is_bestseller")
    @ApiModelProperty("if isBestseller = 1 so the book is considered to be bestseller and if 0 the book is not a " +
            "bestseller")
    private Integer isBestseller;

    @ApiModelProperty("mnemonical identity sequence of characters")
    private String slug;

    @ApiModelProperty("book title")
    private String title;

    @ApiModelProperty("image url")
    private String image;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BookFile> bookFileList = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    @ApiModelProperty("book description text")
    private String description;

    @Column(name = "price")
    @JsonProperty("price")
    @ApiModelProperty("book price without discount")
    private Integer priceOld;

    @Column(name = "discount")
    @JsonProperty("discount value for book")
    private Double price;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<BookRating> bookRatings = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "book2tag", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnore
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "book2genre", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @JsonIgnore
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<BookReview> bookReviews = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<BookUser> bookUsers = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<BookAuthor> bookAuthors = new HashSet<>();

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private Set<BalanceTransaction> balanceTransactions = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<FileDownload> fileDownloads = new HashSet<>();

    @PreRemove
    private void preRemove() {
        balanceTransactions.forEach(transaction -> transaction.setBook(null));
    }

    @JsonProperty
    public Integer discountPrice() {
        return priceOld - Math.toIntExact(Math.round(price * priceOld));
    }

    public Integer getDiscountPercent() {
        return Math.toIntExact(Math.round(price * 100));
    }

    public long getCountRating(int value) {
        return bookRatings.stream()
                .filter(rat -> rat.getValue() == value)
                .count();
    }

    public Integer getAverageRating() {
        if (bookRatings.isEmpty()) {
            return 0;
        }
        return Math.toIntExact(
                Math.round(
                        bookRatings.stream().mapToInt(BookRating::getValue).average().getAsDouble()));
    }

    public Set<BookRating> getBookRatings() {
        return bookRatings;
    }

    public void setBookRatings(Set<BookRating> bookRatings) {
        this.bookRatings = bookRatings;
    }

    public Set<FileDownload> getFileDownloads() {
        return fileDownloads;
    }

    public void setFileDownloads(Set<FileDownload> fileDownloads) {
        this.fileDownloads = fileDownloads;
    }

    public Set<BookReview> getBookReviews() {
        return bookReviews;
    }

    public void setBookReviews(Set<BookReview> bookReviews) {
        this.bookReviews = bookReviews;
    }

    public List<BookFile> getBookFileList() {
        return bookFileList;
    }

    public void setBookFileList(List<BookFile> bookFileList) {
        this.bookFileList = bookFileList;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public Integer getIsBestseller() {
        return isBestseller;
    }

    public void setIsBestseller(Integer isBestseller) {
        this.isBestseller = isBestseller;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(Integer priceOld) {
        this.priceOld = priceOld;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<BookUser> getBookUsers() {
        return bookUsers;
    }

    public void setBookUsers(Set<BookUser> bookUsers) {
        this.bookUsers = bookUsers;
    }

    public Set<BookAuthor> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(Set<BookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public Set<BalanceTransaction> getBalanceTransactions() {
        return balanceTransactions;
    }

    public void setBalanceTransactions(Set<BalanceTransaction> balanceTransactions) {
        this.balanceTransactions = balanceTransactions;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", priceOld='" + priceOld + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
