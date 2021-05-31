package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.model.Book;
import com.example.MyBookShopApp.data.repositories.BookRepository;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.dto.SearchWordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Controller
@RequestMapping("/books")
public class BookShopPostponedController {

    private final BookRepository bookRepository;

    private final BookService bookService;

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute(name = "bookPostponed")
    public List<Book> bookCart() {
        return new ArrayList<>();
    }

    @Autowired
    public BookShopPostponedController(BookRepository bookRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @GetMapping("/postponed")
    public String handlePostponeRequest(@CookieValue(value = "postponedContents", required = false) String postponedContents,
                                        Model model) {
        if (postponedContents == null || postponedContents.equals("")) {
            model.addAttribute("isPostponedEmpty", true);
        } else {
            model.addAttribute("isPostponedEmpty", false);

            model.addAttribute("bookPostponed", bookService.getBooksByCookie(postponedContents, bookRepository));
        }
        return "postponed";
    }

    @PostMapping("/changeBookStatus/postpone/{slug}")
    public String handleChangeBookStatusPostpone(@PathVariable("slug") String slug,
                                                 @CookieValue(value = "postponedContents", required = false) String postponedContents,
                                                 HttpServletResponse response,
                                                 Model model) {
        if (postponedContents == null || postponedContents.equals("")) {
            Cookie cookie = new Cookie("postponedContents", slug);
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isPostponedEmpty", false);
        } else if (!postponedContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(postponedContents).add(slug);
            Cookie cookie = new Cookie("postponedContents", stringJoiner.toString());
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isPostponedEmpty", false);
        }
        return "redirect:/books/" + slug;
    }

    @PostMapping("/changeBookStatus/postponed/remove/{slug}")
    public String handleRemoveBookFromPostponeRequest(
            @PathVariable("slug") String slug,
            @CookieValue(name = "postponedContents", required = false) String postponedContents,
            HttpServletResponse response
    ) {

        bookService.setUpCookie(slug, postponedContents, response, "postponedContents");

        return "redirect:/books/postponed";
    }

    @PostMapping("/changeBookStatus/postponed/cart/{slug}")
    public String handleCartBookFromPostponeRequest(
            @PathVariable("slug") String slug,
            @CookieValue(name = "postponedContents", required = false) String postponedContents,
            @CookieValue(name = "cartContents", required = false) String cartContents,
            HttpServletResponse response
    ) {

        bookService.setUpCookie(slug, postponedContents, response, "postponedContents");
        if (cartContents == null || cartContents.equals("")) {
            Cookie cookie = new Cookie("cartContents", slug);
            cookie.setPath("/books");
            response.addCookie(cookie);
        } else if (!cartContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents).add(slug);
            Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
            cookie.setPath("/books");
            response.addCookie(cookie);
        }

        return "redirect:/books/postponed";
    }

    @PostMapping("/changeBookStatus/postponed/cart/all")
    public String handleCartAllBookFromPostponeRequest(
            @CookieValue(name = "postponedContents", required = false) String postponedContents,
            @CookieValue(name = "cartContents", required = false) String cartContents,
            HttpServletResponse response,
            Model model
    ) {

        ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(postponedContents.split("/")));
        if (cartContents == null || cartContents.equals("")) {
            Cookie cookieCart = new Cookie("cartContents", String.join("/", cookieBooks));
            cookieCart.setPath("/books");
            response.addCookie(cookieCart);
        } else {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents);
            cookieBooks.stream().filter(s -> !cartContents.contains(s)).forEach(stringJoiner::add);
            Cookie cookieCart = new Cookie("cartContents", stringJoiner.toString());
            cookieCart.setPath("/books");
            response.addCookie(cookieCart);
        }
        cookieBooks.clear();
        Cookie postponedCookie = new Cookie("postponedContents", "");
        postponedCookie.setPath("/books");
        response.addCookie(postponedCookie);
        model.addAttribute("isPostponedEmpty", true);
        return "redirect:/books/postponed";
    }
}
