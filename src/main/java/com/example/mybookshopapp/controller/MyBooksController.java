package com.example.mybookshopapp.controller;

import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.entity.Book;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.entity.security.BookstoreUserDetails;
import com.example.mybookshopapp.service.BookService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class MyBooksController {

    private final BookService bookService;

    public MyBooksController(BookService bookService) {
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

    @ModelAttribute("paidBooks")
    public List<Book> paidBooks(@AuthenticationPrincipal BookstoreUserDetails userDetails) {
        return bookService.getPaidBooksForUser(userDetails.getBookstoreUser());
    }

    @ModelAttribute("archivedBooks")
    public List<Book> archivedBooks(@AuthenticationPrincipal BookstoreUserDetails userDetails) {
        return bookService.getArchivedBooksForUser(userDetails.getBookstoreUser());
    }

    @GetMapping("/my")
    public String handleMy() {
        return "my";
    }

    @GetMapping("/myarchive")
    public String handleMyArchive() {
        return "myarchive";
    }
}
