package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.entity.Author;
import com.example.mybookshopapp.entity.Book;
import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.List;

public class BookWithAuthorsDto {

    private String slug;

    private Integer isBestseller;

    private String title;

    private String image;

    private Integer priceOld;

    private Integer discountPrice;

    private Integer discountPercent;

    private List<Author> authorList;

    public BookWithAuthorsDto(Book book, List<Author> author) {
        this.slug = book.getSlug();
        this.isBestseller = book.getIsBestseller();
        this.title = book.getTitle();
        this.image = book.getImage();
        this.priceOld = book.getPriceOld();
        this.discountPrice = book.discountPrice();
        this.discountPercent = book.getDiscountPercent();
        this.authorList = author;
    }

    public BookWithAuthorsDto() {
    }

    @JsonGetter("authors")
    public String authorsName() {
        if (authorList.isEmpty()) {
            return "unknown";
        }
        return authorList.size() == 1 ?
                authorList.get(0).getName() :
                authorList.get(0).getName() + " и другие";
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(Integer priceOld) {
        this.priceOld = priceOld;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }
}
