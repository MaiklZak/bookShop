package com.example.mybookshopapp.controller;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.entity.Author;
import com.example.mybookshopapp.entity.Book;
import com.example.mybookshopapp.service.AuthorService;
import com.example.mybookshopapp.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Api(tags = {"authors-controller"})
@Tag(name = "authors-controller", description = "authors data")
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
        return authorService.getAuthorsMap();
    }

    @GetMapping("/authors")
    public String authorsPage() {
        return "/authors/index";
    }

    @ApiOperation("method to get map of authors")
    @GetMapping("/api/authors")
    @ResponseBody
    public Map<String, List<Author>> authors() {
        return authorService.getAuthorsMap();
    }

    @GetMapping("/authors/{slug}")
    public String authorsSlugPage(@PathVariable String slug, Model model) {
        Author author = authorService.getAuthorBySlug(slug);
        if (author == null) {
            return "redirect:/authors";
        }
        List<Book> books = bookService.getBooksByAuthor(author);
        model.addAttribute("author", author);
        model.addAttribute("booksOfAuthor", bookService.getBookWithAuthorDtoList(books));
        return "authors/slug";
    }

    @GetMapping(value = "/books/author/{slug}", produces = MediaType.TEXT_HTML_VALUE)
    public String booksOfAuthorPage(@PathVariable String slug,
                                    Model model) {
        Author author = authorService.getAuthorBySlug(slug);
        if (author == null) {
            return "redirect:/authors";
        }
        List<Book> books = bookService.getBooksByAuthorPage(author, 0, 20);
        model.addAttribute("booksOfAuthor", bookService.getBookWithAuthorDtoList(books));
        model.addAttribute("author", author);
        return "books/author";
    }

    @GetMapping(value = "/books/author/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BooksPageDto getBooksByAuthor(@PathVariable String slug,
                                         @RequestParam(value = "offset") Integer offset,
                                         @RequestParam(value = "limit") Integer limit) {
        Author author = authorService.getAuthorBySlug(slug);
        List<Book> books = bookService.getBooksByAuthorPage(author, offset, limit);
        return new BooksPageDto(bookService.getBookWithAuthorDtoList(books));
    }
}

