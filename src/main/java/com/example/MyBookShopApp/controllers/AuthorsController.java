package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.AuthorService;
import com.example.MyBookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authors")
public class AuthorsController {

    private final AuthorService authorService;

    private final BookService bookService;

    @Autowired
    public AuthorsController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<Author>> authorsMap() {
        Map<String, List<Author>> authorsMap = authorService.getAuthorsMap();
        return authorsMap;
    }

    @GetMapping("")
    public String authorsPage() {
        return "/authors/index";
    }

    @GetMapping("/slug")
    public String authorPage(@RequestParam Integer id, Model model) {
        model.addAttribute("author", authorService.getAuthorById(id));
        model.addAttribute("books", bookService.getBookPopular());
        return "authors/slug";
    }

    @GetMapping("/books")
    public String BooksByAuthor(@RequestParam Integer id, Model model) {
        model.addAttribute("author", authorService.getAuthorById(id));
        model.addAttribute("books", bookService.getBookPopular());
        return "books/author";
    }
}
