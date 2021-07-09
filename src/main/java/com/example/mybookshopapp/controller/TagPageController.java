package com.example.mybookshopapp.controller;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.entity.security.BookstoreUserDetails;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.service.TagService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TagPageController {

    private final TagService tagService;

    private final BookService bookService;

    public TagPageController(TagService tagService, BookService bookService) {
        this.tagService = tagService;
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
