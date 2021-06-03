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
import java.util.List;
import java.util.StringJoiner;

@Controller
@RequestMapping("/books")
public class BookShopCartController {

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute(name = "bookCart")
    public List<Book> bookCart() {
        return new ArrayList<>();
    }

    private final BookRepository bookRepository;

    private final BookService bookService;

    @Autowired
    public BookShopCartController(BookRepository bookRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
                                    Model model) {
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("isCartEmpty", true);
        } else {
            List<Book> books = bookService.getBooksByCookie(cartContents, bookRepository);
            model.addAttribute("isCartEmpty", false);
            model.addAttribute("bookCart", books);
            model.addAttribute("allPriceOld", books.stream().mapToInt(Book::getPriceOld).sum());
            model.addAttribute("allPrice", books.stream().mapToInt(Book::discountPrice).sum());
        }
        return "cart";
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@PathVariable("slug") String slug, @CookieValue(name = "cartContents",
            required = false) String cartContents, HttpServletResponse response, Model model) {
        if (cartContents != null && !cartContents.equals("")) {
            bookService.setUpCookie(slug, cartContents, response, "cartContents");
            model.addAttribute("isCartEmpty", false);
        } else {
            model.addAttribute("isCartEmpty", true);
        }
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/{slug}")
    public String handleChangeBookStatus(@PathVariable("slug") String slug, @CookieValue(name = "cartContents",
            required = false) String cartContents, HttpServletResponse response, Model model) {

        if (cartContents == null || cartContents.equals("")) {
            Cookie cookie = new Cookie("cartContents", slug);
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else if (!cartContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(cartContents).add(slug);
            Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        }
        return "redirect:/books/" + slug;
    }

    @PostMapping("/changeBookStatus/cart/postponed/{slug}")
    public String handlePostponedBookFromCartRequest(
            @PathVariable("slug") String slug,
            @CookieValue(name = "postponedContents", required = false) String postponedContents,
            @CookieValue(name = "cartContents", required = false) String cartContents,
            HttpServletResponse response
    ) {

        bookService.setUpCookie(slug, cartContents, response, "cartContents");
        if (postponedContents == null || postponedContents.equals("")) {
            Cookie cookiePostpone = new Cookie("postponedContents", slug);
            cookiePostpone.setPath("/books");
            response.addCookie(cookiePostpone);
        } else if (!postponedContents.contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(postponedContents).add(slug);
            Cookie cookiePostpone = new Cookie("postponedContents", stringJoiner.toString());
            cookiePostpone.setPath("/books");
            response.addCookie(cookiePostpone);
        }
        return "redirect:/books/cart";
    }

}
