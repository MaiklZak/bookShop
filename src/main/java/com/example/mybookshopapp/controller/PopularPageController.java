package com.example.mybookshopapp.controller;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.Book;
import com.example.mybookshopapp.entity.security.BookstoreUserDetails;
import com.example.mybookshopapp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PopularPageController {

    private final BookService bookService;

    @Autowired
    public PopularPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("popularBooks")
    public List<Book> popularBooks(@AuthenticationPrincipal BookstoreUserDetails userDetails,
                                   @CookieValue(value = "userHash", required = false) String userHash) {
        if (userDetails != null) {
            return bookService.getPageOfPopularBooksForUser(userDetails.getBookstoreUser(), 0, 20);
        }
        return bookService.getPageOfPopularBooksForNotAuthenticatedUser(userHash, 0, 20);
    }

    @GetMapping(value = "/books/popular", produces = MediaType.TEXT_HTML_VALUE)
    public String getPopularPage() {
        return "books/popular";
    }

    @GetMapping(value = "/books/popular", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BooksPageDto getPopularBooksPage(@AuthenticationPrincipal BookstoreUserDetails userDetails,
                                            @RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit,
                                            @CookieValue(value = "userHash", required = false) String userHash) {

        if (userDetails != null) {
            return new BooksPageDto(bookService.getPageOfPopularBooksForUser(userDetails.getBookstoreUser(), offset, limit));
        }
        return new BooksPageDto(bookService.getPageOfPopularBooksForNotAuthenticatedUser(userHash, offset, limit));
    }
}