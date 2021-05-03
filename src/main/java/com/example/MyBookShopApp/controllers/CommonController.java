package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CommonController {

    private final BookService bookService;

    public CommonController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping({"/search/{query}", "/search"})
    public String search(@PathVariable(name = "query", required = false) String query, Model model) {
        model.addAttribute("books", bookService.getBookByTitle(query));
        return "books/search";
    }

    @GetMapping("/documents")
    public String documentsPage() {
        return "info/documents";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "info/about";
    }

    @GetMapping("/faq")
    public String faqPage() {
        return "info/faq";
    }

    @GetMapping("/contacts")
    public String contactsPage() {
        return "info/contacts";
    }

    @GetMapping("/signin")
    public String signinPage() {
        return "signin";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }
}
