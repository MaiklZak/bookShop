package com.example.mybookshopapp.controller;

import com.example.mybookshopapp.dto.ChangeStatusPayload;
import com.example.mybookshopapp.entity.Book;
import com.example.mybookshopapp.entity.TypeBookToUser;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.entity.security.BookstoreUserDetails;
import com.example.mybookshopapp.repository.BookRepository;
import com.example.mybookshopapp.repository.security.BookstoreUserRepository;
import com.example.mybookshopapp.service.BookUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BookShopCartController {

    private static final String IS_CART_EMPTY = "isCartEmpty";

    private final BookRepository bookRepository;
    private final BookstoreUserRepository bookstoreUserRepository;
    private final BookUserService bookUserService;

    @Autowired
    public BookShopCartController(BookRepository bookRepository, BookstoreUserRepository bookstoreUserRepository, BookUserService bookUserService) {
        this.bookRepository = bookRepository;
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.bookUserService = bookUserService;
    }

    @ModelAttribute(name = "bookCart")
    public List<Book> bookCart() {
        return new ArrayList<>();
    }

    @GetMapping("/cart")
    public String handleCartRequest(@AuthenticationPrincipal BookstoreUserDetails user,
                                    @CookieValue(value = "userHash", required = false) String userHash,
                                    HttpServletResponse response,
                                    Model model) {
        List<Book> booksCart;

        if (user != null) {
            bookUserService.moveBooksFromUserHashToCurrentUser(userHash, user, response);
            booksCart = bookRepository.findBooksByUserAndType(user.getBookstoreUser(), TypeBookToUser.CART);
        } else if (userHash != null && !userHash.equals("")) {
            BookstoreUser bookstoreUserByHash = bookstoreUserRepository.findBookstoreUserByHash(userHash);
            booksCart = bookRepository.findBooksByUserAndType(bookstoreUserByHash, TypeBookToUser.CART);
        } else {
            model.addAttribute(IS_CART_EMPTY, true);
            return "cart";
        }

        model.addAttribute(IS_CART_EMPTY, booksCart.isEmpty());
        model.addAttribute("bookCart", booksCart);
        model.addAttribute("allPriceOld", booksCart.stream().mapToInt(Book::getPriceOld).sum());
        model.addAttribute("allPrice", booksCart.stream().mapToInt(Book::discountPrice).sum());
        return "cart";
    }

    @PostMapping("/changeBookStatus/cart")
    public String handleChangeBookStatus(@AuthenticationPrincipal BookstoreUserDetails userDetails,
                                         @RequestBody ChangeStatusPayload payload,
                                         @CookieValue(name = "userHash", required = false) String userHash,
                                         HttpServletResponse response) {
        bookUserService.changeBookStatus(payload, userDetails, userHash, response);
        return "redirect:/books/" + payload.getBooksIds();
    }

    @PostMapping("/changeBookStatus/cart/remove/")
    public String handleRemoveBookFromCartRequest(@AuthenticationPrincipal BookstoreUserDetails userDetails,
                                                  @RequestBody ChangeStatusPayload payload,
                                                  @CookieValue(name = "userHash", required = false) String userHash) {
        TypeBookToUser type = TypeBookToUser.getTypeByString(payload.getStatus());
        if (userDetails != null) {
            bookUserService.changeBookStatusForUser(payload.getBooksIds(), userDetails.getBookstoreUser(), type);
        } else {
            BookstoreUser bookstoreUserByHash = bookstoreUserRepository.findBookstoreUserByHash(userHash);
            bookUserService.changeBookStatusForUser(payload.getBooksIds(), bookstoreUserByHash, type);
        }
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/cart/postpone/")
    public String handlePostponedBookFromCartRequest(@AuthenticationPrincipal BookstoreUserDetails userDetails,
                                                     @RequestBody ChangeStatusPayload payload,
                                                     @CookieValue(name = "userHash", required = false) String userHash,
                                                     HttpServletResponse response) {
        bookUserService.changeBookStatus(payload, userDetails, userHash, response);
        return "redirect:/books/cart";
    }

}
