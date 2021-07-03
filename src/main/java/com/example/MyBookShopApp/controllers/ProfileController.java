package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BalanceTransactionService;
import com.example.MyBookShopApp.data.PaymentService;
import com.example.MyBookShopApp.data.dto.BalanceTransactionDto;
import com.example.MyBookShopApp.errs.IncorrectAmountToEnterException;
import com.example.MyBookShopApp.errs.NoEnoughFundsForPayment;
import com.example.MyBookShopApp.errs.WrongCredentialsException;
import com.example.MyBookShopApp.security.BookstoreUser;
import com.example.MyBookShopApp.security.BookstoreUserDetails;
import com.example.MyBookShopApp.security.BookstoreUserRegister;
import com.example.MyBookShopApp.security.ChangeUserForm;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

@Controller
public class ProfileController {

    private final PaymentService paymentService;
    private final BalanceTransactionService balanceTransactionService;
    private final BookstoreUserRegister userRegister;

    public ProfileController(PaymentService paymentService, BalanceTransactionService balanceTransactionService, BookstoreUserRegister userRegister) {
        this.paymentService = paymentService;
        this.balanceTransactionService = balanceTransactionService;
        this.userRegister = userRegister;
    }

    @GetMapping("/profile")
    public String handleProfile(@RequestParam(name = "part", required = false) String part, Model model) {
        BookstoreUser user = (BookstoreUser) userRegister.getCurrentUser();
        model.addAttribute("curUsr", user);
        model.addAttribute("part", part);
        model.addAttribute("transactions", balanceTransactionService.getTransactionsByUserPage(user, 0, 50).getContent());
        return "profile";
    }

    @PostMapping("/profile")
    public String handleChangeProfile(ChangeUserForm changeUserForm) throws WrongCredentialsException {
        userRegister.updateUser(changeUserForm);
        return "redirect:/profile";
    }

    @GetMapping("/transactions")
    @ResponseBody
    public BalanceTransactionDto getNextPageTransactions(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        BookstoreUser user = (BookstoreUser) userRegister.getCurrentUser();
        return new BalanceTransactionDto(balanceTransactionService.getTransactionsByUserPage(user, offset, limit).getContent());
    }

    @GetMapping("/pay")
    public String handlePay(@AuthenticationPrincipal BookstoreUserDetails user) throws NoEnoughFundsForPayment {
        paymentService.buyBooksByUser(user.getBookstoreUser());
        return "redirect:/books/cart";
    }

    @PostMapping("/payment")
    public RedirectView handlePayment(@AuthenticationPrincipal BookstoreUserDetails user,
                                      @RequestParam(required = false) Integer sum) throws NoSuchAlgorithmException, IncorrectAmountToEnterException {
        if (sum == null || sum <= 0) {
            throw new IncorrectAmountToEnterException("Amount to enter must be more 0");
        }
        String paymentUrl = paymentService.deposit(user, sum);
        return new RedirectView(paymentUrl);
    }

    @GetMapping("/changeCredentials/{updateUserId}/{currentUserId}/{code}")
    public String approveCredentials(@PathVariable Integer updateUserId,
                                     @PathVariable Integer currentUserId,
                                     @PathVariable String code,
                                     HttpServletResponse response,
                                     Model model) throws WrongCredentialsException {

        String token = userRegister.approveCredentials(updateUserId, currentUserId, code);
        BookstoreUser user = (BookstoreUser) userRegister.getCurrentUser();
        userRegister.authenticateUpdatedUser(user, token);
        response.addCookie(new Cookie("token", token));
        model.addAttribute("curUsr", user);
        model.addAttribute("part", null);
        model.addAttribute("credentialsSuccess", "Профиль успешно сохранен");
        model.addAttribute("transactions", balanceTransactionService.getTransactionsByUserPage(user, 0, 50).getContent());
        return "/profile";
    }

}
