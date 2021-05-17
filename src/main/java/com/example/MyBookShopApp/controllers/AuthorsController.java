package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.dto.BooksPageDto;
import com.example.MyBookShopApp.data.dto.SearchWordDto;
import com.example.MyBookShopApp.data.model.Author;
import com.example.MyBookShopApp.data.AuthorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Api(description = "authors data")
public class AuthorsController {

    private final AuthorService authorService;

    private final BookService bookService;

    @Autowired
    public AuthorsController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
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

    @GetMapping("/authors/SLUG")
    public String authorSlugPage(@RequestParam("id") Integer id, Model model) {
        Author author = authorService.getAuthorById(id);
        model.addAttribute("author", authorService.getAuthorById(id));
        model.addAttribute("books", bookService.getBooksByAuthorId(0, 10, id));
        model.addAttribute("descriptionList", bookService.getListOfDescription(author.getDescription()));
        return "authors/slug";
    }

    @GetMapping("/books/author/SLUG")
    public String booksOfAuthorPage(@RequestParam("id") Integer id, Model model) {
        model.addAttribute("author", authorService.getAuthorById(id));
        model.addAttribute("books", bookService.getBooksByAuthorId(0, 20, id));
        return "books/author";
    }

    @GetMapping("/books/author/{id}")
    @ResponseBody
    public BooksPageDto getBooksByAuthor(Integer offset, Integer limit, @PathVariable("id") Integer id) {
        return new BooksPageDto(bookService.getBooksByAuthorId(offset, limit, id));
    }


}
