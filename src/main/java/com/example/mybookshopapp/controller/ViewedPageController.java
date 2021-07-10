package com.example.mybookshopapp.controller;

import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.Book;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.entity.security.BookstoreUserDetails;
import com.example.mybookshopapp.service.BookService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ViewedPageController {

    private final BookService bookService;

    public ViewedPageController(BookService bookService) {
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

    @ModelAttribute("viewedBooks")
    public List<Book> viewedBooks(@AuthenticationPrincipal BookstoreUserDetails userDetails) {
        return bookService.getPageOfViewedBooksByUser(userDetails.getBookstoreUser(), 0, 20);
    }

    @GetMapping("/viewed")
    public String viewedPage() {
        return "books/viewed";
    }

    @GetMapping("/books/viewed")
    @ResponseBody
    public BooksPageDto getPopularBooksPage(@AuthenticationPrincipal BookstoreUserDetails userDetails,
                                            @RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfViewedBooksByUser(userDetails.getBookstoreUser(), offset, limit));
    }
}