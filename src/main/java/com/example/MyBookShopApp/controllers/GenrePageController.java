package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.GenreService;
import com.example.MyBookShopApp.data.dto.BooksPageDto;
import com.example.MyBookShopApp.data.dto.SearchWordDto;
import com.example.MyBookShopApp.data.model.Genre;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GenrePageController {

    private final GenreService genreService;

    private final BookService bookService;

    public GenrePageController(GenreService genreService, BookService bookService) {
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/genres")
    public String getGenrePage(Model model) {
        model.addAttribute("genresRoot", genreService.getGenresWithoutParent());
        model.addAttribute("genresDto", genreService.getListGenreDto());
        return "genres/index";
    }

    @GetMapping("/genres/SLUG")
    public String getGenreSlugPage(@RequestParam("id") Integer id, Model model) {
        Genre genre = genreService.getGenreById(id);
        model.addAttribute("parentGenre", genre.getParentId() != null ? genreService.getGenreById(genre.getParentId()) : null);
        model.addAttribute("genre", genre);
        model.addAttribute("books", bookService.getBooksByGenreId(0, 20, id));
        return "genres/slug";
    }

    @GetMapping("/books/genre/{id}")
    @ResponseBody
    public BooksPageDto getBooksByGenre(Integer offset, Integer limit, @PathVariable("id") Integer id) {
        return new BooksPageDto(bookService.getBooksByGenreId(offset, limit, id));
    }
}
