package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BooksController {

    private final BookService bookService;

    @Autowired
    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("books/slug")
    public String bookPage(@RequestParam Integer id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "books/slug";
    }

    @GetMapping("/genres")
    public String genresPage() {
        return "genres/index";
    }

    @GetMapping("/popular")
    public String popularPage(Model model) {
        model.addAttribute("popularBooks", bookService.getBookPopular());
        return "books/popular";
    }

    @GetMapping("/recent")
    public String recentPage(Model model) {
        model.addAttribute("newBooks", bookService.getBookNews());
        return "books/recent";
    }

    @GetMapping("/postponed")
    public String postponedPage() {
        return "books/postponed";
    }


}


