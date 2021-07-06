package com.example.mybookshopapp.controller;

import com.example.mybookshopapp.entity.Book;
import com.example.mybookshopapp.entity.Tag;
import com.example.mybookshopapp.entity.security.BookstoreUserDetails;
import com.example.mybookshopapp.service.BookService;
import com.example.mybookshopapp.dto.BooksPageDto;
import com.example.mybookshopapp.dto.SearchWordDto;
import com.example.mybookshopapp.errs.EmptySearchException;


import com.example.mybookshopapp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MainPageController {

    private final BookService bookService;
    private final TagService tagService;

    @ModelAttribute("popularBooks")
    public List<Book> popularBooks() {
        return bookService.getPageOfPopularBooks(0, 6);
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResult")
    public List<Book> searchResult() {
        return new ArrayList<>();
    }

    @ModelAttribute("tags")
    public Map<Tag, Integer> tagsOfBooks() {
        return tagService.getTagsAndCount();
    }

    @Autowired
    public MainPageController(BookService bookService, TagService tagService) {
        this.bookService = bookService;
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String mainPage(@AuthenticationPrincipal BookstoreUserDetails user,
                           @CookieValue(value = "userHash", required = false) String userHash,
                           HttpServletResponse response,
                           Model model) {
        if (user != null) {
            bookService.moveBooksFromUserHashToCurrentUser(userHash, user, response);
            model.addAttribute("recommendedBooks",
                    bookService.getPageOfRecommendedBooksForUser(user.getBookstoreUser(), 0, 6));
        } else {
            model.addAttribute("recommendedBooks",
                    bookService.getPageOfRecommendedBooksForNotAuthenticatedUser(userHash, 0, 6));
        }
        return "index";
    }

    @GetMapping("/books/recommended")
    @ResponseBody
    public BooksPageDto getBooksPage(@AuthenticationPrincipal BookstoreUserDetails userDetails,
                                     @CookieValue(value = "userHash", required = false) String userHash,
                                     @RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit) {
        if (userDetails != null) {
            return new BooksPageDto(bookService.getPageOfRecommendedBooksForUser(userDetails.getBookstoreUser(), offset, limit));
        }
        return new BooksPageDto(bookService.getPageOfRecommendedBooksForNotAuthenticatedUser(userHash, offset, limit));
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                  Model model) throws EmptySearchException {
        if (searchWordDto != null) {
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("searchResults",
                    bookService.getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(), 0, 5));
            return "/search/index";
        } else {
            throw new EmptySearchException("Поиск по null невозможен");
        }
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        return new BooksPageDto(bookService.getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(), offset, limit));
    }
}