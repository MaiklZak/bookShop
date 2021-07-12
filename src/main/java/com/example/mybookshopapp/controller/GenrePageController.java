package com.example.mybookshopapp.controller;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.entity.security.BookstoreUserDetails;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.GenreService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @ModelAttribute("curUsr")
    public BookstoreUser getCurrentUser(@AuthenticationPrincipal BookstoreUserDetails userDetails) {
        if (userDetails != null) {
            return userDetails.getBookstoreUser();
        }
        return null;
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
