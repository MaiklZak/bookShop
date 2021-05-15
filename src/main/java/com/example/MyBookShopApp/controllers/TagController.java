package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.TagService;
import com.example.MyBookShopApp.data.dto.BooksPageDto;
import com.example.MyBookShopApp.data.dto.SearchWordDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TagController {

    private final TagService tagService;

    private final BookService bookService;

    public TagController(TagService tagService, BookService bookService) {
        this.tagService = tagService;
        this.bookService = bookService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/tags")
    public String getTagPage(@RequestParam("tag") Integer id, Model model) {
        model.addAttribute("tag", tagService.getById(id));
        model.addAttribute("booksByTag", bookService.getBookByTag(0, 20, id));
        return "tags/index";
    }

    @GetMapping("/books/tag/{id}")
    @ResponseBody
    public BooksPageDto getBooksPage(@RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit,
                                     @PathVariable("id") Integer id) {
        return new BooksPageDto(bookService.getBookByTag(offset, limit, id));
    }
}
