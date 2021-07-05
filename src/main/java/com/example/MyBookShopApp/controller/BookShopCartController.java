package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.entity.Book;
import com.example.MyBookShopApp.entity.BookUser;
import com.example.MyBookShopApp.entity.BookUserType;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.repository.BookUserRepository;
import com.example.MyBookShopApp.security.entity.BookstoreUser;
import com.example.MyBookShopApp.security.entity.BookstoreUserDetails;
import com.example.MyBookShopApp.security.repository.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/books")
public class BookShopCartController {

    @ModelAttribute(name = "bookCart")
    public List<Book> bookCart() {
        return new ArrayList<>();
    }

    private final BookRepository bookRepository;
    private final BookService bookService;
    private final BookstoreUserRepository bookstoreUserRepository;
    private final BookUserRepository bookUserRepository;

    @Autowired
    public BookShopCartController(BookRepository bookRepository, BookService bookService, BookstoreUserRepository bookstoreUserRepository, BookUserRepository bookUserRepository) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.bookUserRepository = bookUserRepository;
    }

    @GetMapping("/cart")
    public String handleCartRequest(@AuthenticationPrincipal BookstoreUserDetails user,
                                    @CookieValue(value = "userHash", required = false) String userHash,
                                    HttpServletResponse response,
                                    Model model) {

        if (user != null) {
            BookstoreUser bookstoreUserByHash = bookstoreUserRepository.findBookstoreUserByHash(userHash);
            if (userHash != null && !userHash.equals("") && bookstoreUserByHash != null) {
                List<Book> booksFromCookieUser = bookRepository.findBooksByUser(bookstoreUserByHash);
                for (Book book : booksFromCookieUser) {
                    BookUser bookUserFromCookieUser = bookUserRepository.findByBookAndUser(book, bookstoreUserByHash);
                    BookUser bookUserFromCurrentUser = bookUserRepository.findByBookAndUser(book, user.getBookstoreUser());
                    if (bookUserFromCurrentUser != null) {
                        bookUserRepository.delete(bookUserFromCookieUser);
                    } else {
                        bookUserFromCookieUser.setUser(user.getBookstoreUser());
                        bookUserRepository.save(bookUserFromCookieUser);
                    }
                }
                bookstoreUserRepository.delete(bookstoreUserByHash);
                response.addCookie(new Cookie("cartContents", ""));
            }
            List<Book> booksByUser = bookRepository.findBooksByUserAndType(user.getBookstoreUser(), BookUserType.CART);
            model.addAttribute("isCartEmpty", booksByUser.isEmpty());
            model.addAttribute("bookCart", booksByUser);
            return "cart";
        }
        if (userHash == null || userHash.equals("")) {
            model.addAttribute("isCartEmpty", true);
        } else {
            BookstoreUser bookstoreUserByHash = bookstoreUserRepository.findBookstoreUserByHash(userHash);
            List<Book> booksFromCookieUser = bookRepository.findBooksByUserAndType(bookstoreUserByHash, BookUserType.CART);
            model.addAttribute("isCartEmpty", booksFromCookieUser.isEmpty());
            model.addAttribute("bookCart", booksFromCookieUser);
        }
        return "cart";
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@AuthenticationPrincipal BookstoreUserDetails user,
                                                  @PathVariable("slug") String slug,
                                                  @CookieValue(name = "userHash", required = false) String userHash,
                                                  Model model) {
        if (user != null) {
            bookService.removeBookFromCartBySlag(user.getBookstoreUser(), slug);
            return "redirect:/books/cart";
        }
        if (userHash != null && !userHash.equals("")) {
            BookstoreUser bookstoreUserByHash = bookstoreUserRepository.findBookstoreUserByHash(userHash);
            bookService.removeBookFromCartBySlag(bookstoreUserByHash, slug);
            model.addAttribute("isCartEmpty", bookRepository.findBooksByUserAndType(bookstoreUserByHash, BookUserType.CART));
        } else {
            model.addAttribute("isCartEmpty", true);
        }
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/{slug}")
    public String handleChangeBookStatus(@AuthenticationPrincipal BookstoreUserDetails user,
                                         @PathVariable("slug") String slug,
                                         @CookieValue(name = "userHash", required = false) String userHash,
                                         HttpServletResponse response) {
        if (user != null) {
            bookService.changeBookStatusToCartForUser(BookUserType.CART, slug, user.getBookstoreUser());
            return "redirect:/books/" + slug;
        }
        if (userHash != null && !userHash.equals("")) {
            BookstoreUser bookstoreUserByHash = bookstoreUserRepository.findBookstoreUserByHash(userHash);
            bookService.changeBookStatusToCartForUser(BookUserType.CART, slug, bookstoreUserByHash);
        } else {
            BookstoreUser defaultUser = new BookstoreUser();
            defaultUser.setHash(UUID.randomUUID().toString());
            defaultUser = bookstoreUserRepository.save(defaultUser);
            bookService.changeBookStatusForUser(bookRepository.findBookBySlug(slug), defaultUser, BookUserType.CART);

            Cookie cookie = new Cookie("userHash", defaultUser.getHash());
            response.addCookie(cookie);
        }
        return "redirect:/books/" + slug;
    }

}
