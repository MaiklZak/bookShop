package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.model.Book;
import com.example.MyBookShopApp.data.model.BookUserType;
import com.example.MyBookShopApp.data.repositories.BookRepository;
import com.example.MyBookShopApp.security.BookstoreUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
public class BookShopCartController {

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
    public String handleCartRequest(@AuthenticationPrincipal BookstoreUserDetails user,
                                    @CookieValue(value = "cartContents", required = false) String cartContents,
                                    HttpServletResponse response,
                                    Model model) {

        if (user != null) {
            if (cartContents != null && !cartContents.equals("")) {
                cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
                cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
                String[] cookieSlugs = cartContents.split("/");
                List<Book> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(cookieSlugs);
                for (Book book : booksFromCookieSlugs) {
                    bookService.changeBookStatusForUser(BookUserType.CART, book.getSlug());
                }
                response.addCookie(new Cookie("cartContents", ""));
            }
            List<Book> booksByUser = bookRepository.findBooksByUserAndType(user.getBookstoreUser(), BookUserType.CART);
            model.addAttribute("isCartEmpty", booksByUser.isEmpty());
            model.addAttribute("bookCart", booksByUser);
            return "cart";
        }
        if (cartContents == null || cartContents.equals("")) {
            model.addAttribute("isCartEmpty", true);
        } else {
            model.addAttribute("isCartEmpty", false);
            cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
            cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
            String[] cookieSlugs = cartContents.split("/");
            List<Book> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(cookieSlugs);
            model.addAttribute("bookCart", booksFromCookieSlugs);
        }
        return "cart";
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@AuthenticationPrincipal BookstoreUserDetails user,
                                                  @PathVariable("slug") String slug,
                                                  @CookieValue(name = "cartContents", required = false) String cartContents,
                                                  HttpServletResponse response,
                                                  Model model) {
        if (user != null) {
            bookService.removeBookFromCartBySlag(user.getBookstoreUser(), slug);
            return "redirect:/books/cart";
        }
        if (cartContents != null && !cartContents.equals("")) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("cartContents", String.join("/", cookieBooks));
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else {
            model.addAttribute("isCartEmpty", true);
        }
        return "redirect:/books/cart";
    }

//    @PostMapping("/changeBookStatus/{slug}")
//    public String handleChangeBookStatus(@PathVariable("slug") String slug, @CookieValue(name = "cartContents",
//            required = false) String cartContents, HttpServletResponse response, Model model) {
//
//        if (cartContents == null || cartContents.equals("")) {
//            Cookie cookie = new Cookie("cartContents", slug);
//            cookie.setPath("/books");
//            response.addCookie(cookie);
//            model.addAttribute("isCartEmpty", false);
//        } else if (!cartContents.contains(slug)) {
//            StringJoiner stringJoiner = new StringJoiner("/");
//            stringJoiner.add(cartContents).add(slug);
//            Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
//            cookie.setPath("/books");
//            response.addCookie(cookie);
//            model.addAttribute("isCartEmpty", false);
//        }
//        return "redirect:/books/" + slug;
//    }

    @PostMapping("/changeBookStatus/{slug}")
    public String handleChangeBookStatus(@AuthenticationPrincipal BookstoreUserDetails user,
                                         @PathVariable("slug") String slug,
                                         @CookieValue(name = "cartContents", required = false) String cartContents,
                                         HttpServletResponse response, Model model) {
        if (user != null) {
            bookService.changeBookStatusForUser(BookUserType.CART, slug);
            return "redirect:/books/" + slug;
        }
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

//    @GetMapping("/pay")
//    public RedirectView handlePay(@CookieValue(value = "cartContents", required = false) String cartContents) throws NoSuchAlgorithmException {
//        cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
//        cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
//        String[] cookieSlugs = cartContents.split("/");
//        List<Book> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(cookieSlugs);
//        String paymentUrl = paymentService.getPaymentUrl(booksFromCookieSlugs);
//        return new RedirectView(paymentUrl);
//    }


}
