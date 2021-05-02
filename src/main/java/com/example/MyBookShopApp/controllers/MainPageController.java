package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainPageController {

    private final BookService bookService;

    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks() {
        return bookService.getBookData();
    }

    @ModelAttribute("bookSearch")
    public Book bookSearch() {
        return new Book();
    }

    @Autowired
    public MainPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/signin")
    public String signinPage() {
        return "signin";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping({"/search/{query}", "/search"})
    public String search(@PathVariable(name = "query",required = false) String query, Model model) {
        model.addAttribute("books", bookService.getBookByTitle(query));
        return "books/search";
    }
}
