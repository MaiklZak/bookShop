package com.example.mybookshopapp.controller;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.GenreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GenrePageController {

    private final GenreService genreService;
    private final BookService bookService;

    public GenrePageController(GenreService genreService, BookService bookService) {
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @GetMapping("/genres")
    public String getGenrePage(Model model) {
        model.addAttribute("genresRoot", genreService.getGenresWithoutParent());
        model.addAttribute("genresDto", genreService.getListGenreDto());
        return "genres/index";
    }

    @GetMapping("/genres/slug")
    public String getGenreSlugPage(@RequestParam("id") Integer id, Model model) {
        model.addAttribute("genre", genreService.getGenreById(id));
        model.addAttribute("books", bookService.getBooksByGenreId(0, 20, id));
        return "genres/slug";
    }

    @GetMapping("/books/genre/{id}")
    @ResponseBody
    public BooksPageDto getBooksByGenre(Integer offset, Integer limit, @PathVariable("id") Integer id) {
        return new BooksPageDto(bookService.getBooksByGenreId(offset, limit, id));
    }
}
