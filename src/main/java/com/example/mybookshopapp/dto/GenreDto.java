package com.example.mybookshopapp.dto;



import com.example.mybookshopapp.entity.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GenreDto {

    private Genre parentGenre;

    private Genre genre;

    private Integer countBooks;

    private List<Genre> childGenres;


    public GenreDto(Genre genre, List<Genre> childGenres, Genre parentGenre) {
        this.genre = genre;
        this.childGenres = new ArrayList<>(childGenres);
        this.parentGenre = parentGenre;
        this.countBooks = genre.getBooks().size();
    }

    public Integer getCountBooks() {
        return countBooks;
    }

    public void setCountBooks(Integer countBooks) {
        this.countBooks = countBooks;
    }

    public Genre getParentGenre() {
        return parentGenre;
    }

    public void setParentGenre(Genre parentGenre) {
        this.parentGenre = parentGenre;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Genre> getChildGenres() {
        return childGenres.stream()
                .sorted((o1, o2) -> o2.getBooks().size() - o1.getBooks().size())
                .collect(Collectors.toList());
    }

    public void setChildGenres(List<Genre> childGenres) {
        this.childGenres = childGenres;
    }
}
