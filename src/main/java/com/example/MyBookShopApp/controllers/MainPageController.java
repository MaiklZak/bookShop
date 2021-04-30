package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.AuthorService;
import com.example.MyBookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bookshop")
public class MainPageController {

    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public MainPageController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping("/main")
    public String mainPage(Model model) {
        model.addAttribute("bookData", bookService.getBookData());
        return "index";
    }

    @GetMapping("/genres")
    public String genresPage() {
        return "genres/index";
    }

    @GetMapping("/authors")
    public String authorsPage(Model model) {
        Map<Character, List<Author>> authors = authorService.getAuthors().stream()
                .collect(Collectors.groupingBy(author -> author.getFirstName().charAt(0)));
        System.out.println(authors);
//        authors.sort(Comparator.comparing(Author::getFirstName));
        model.addAttribute("authors", authors);
        return "authors/index";
    }

    @GetMapping("/authors/slug")
    public String authorSlug(@RequestParam Integer id, Model model) {
        model.addAttribute("author", authorService.get(id));
        model.addAttribute("books", bookService.getBookByAuthor(id));
        return "authors/slug";
    }
}
