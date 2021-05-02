package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BooksController {

    @GetMapping("/genres")
    public String genresPage() {
        return "genres/index";
    }

    @GetMapping("/popular")
    public String popularPage() {
        return "books/popular";
    }

    @GetMapping("/recent")
    public String recentPage() {
        return "books/recent";
    }

    @GetMapping("/postponed")
    public String postponedPage() {
        return "books/postponed";
    }
}


