package com.example.mybookshopapp.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

public class BookNewDto {

    @NotNull(message = "image must be chosen")
    private MultipartFile image;

    @NotNull(message = "file must be chosen")
    private MultipartFile fileBook;

    @NotBlank(message = "title must not be empty")
    private String title;

    @NotBlank(message = "author must not be empty")
    @Pattern(regexp = "([а-яА-Яa-zA-Z]+[\\s]?(,\\s)?)+$", message = "invalid characters, authors must be separated by comma with space")
    private String author;

    @NotBlank(message = "genre must not be empty")
    @Pattern(regexp = "([а-яА-Яa-zA-Z]+[\\s]?(,\\s)?)+$", message = "invalid characters, genres must be separated by comma with space")
    private String genre;

    @NotBlank(message = "tag must not be empty")
    @Pattern(regexp = "([а-яА-Яa-zA-Z]+[\\s]?(,\\s)?)+$", message = "invalid characters, tags must be separated by comma with space")
    private String tag;

    @NotNull(message = "price must be set")
    @Min(value = 10, message = "price must be between 10 and 5000")
    @Max(value = 5000, message = "price must be between 10 and 5000")
    private Integer price;

    @Min(value = 0, message = "discount must be between 0 and 99")
    @Max(value = 90, message = "discount must be between 0 and 99")
    private Integer discount;

    @NotBlank(message = "description must not be empty")
    private String description;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public MultipartFile getFileBook() {
        return fileBook;
    }

    public void setFileBook(MultipartFile fileBook) {
        this.fileBook = fileBook;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
