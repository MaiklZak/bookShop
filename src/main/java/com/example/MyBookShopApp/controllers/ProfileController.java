package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BalanceTransactionService;
import com.example.MyBookShopApp.data.PaymentService;
import com.example.MyBookShopApp.errs.NoEnoughFundsForPayment;
import com.example.MyBookShopApp.errs.WrongCredentialsException;
import com.example.MyBookShopApp.security.BookstoreUser;
import com.example.MyBookShopApp.security.BookstoreUserDetails;
import com.example.MyBookShopApp.security.BookstoreUserRegister;
import com.example.MyBookShopApp.security.ChangeUserForm;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

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
        model.addAttribute("transactions", balanceTransactionService.findTransactionsByUser(user));
        model.addAttribute("part", part);
        return "profile";
    }

    @PostMapping("/profile")
    public String handleChangeProfile(ChangeUserForm changeUserForm) throws WrongCredentialsException {
        userRegister.updateUser(changeUserForm);
        return "redirect:/profile";
    }

    @GetMapping("/pay")
    public String handlePay(@AuthenticationPrincipal BookstoreUserDetails user) throws NoEnoughFundsForPayment {
        paymentService.buyBooksByUser(user.getBookstoreUser());
        return "redirect:/books/cart";
    }

    @PostMapping("/payment")
    public RedirectView handlePayment(@AuthenticationPrincipal BookstoreUserDetails user,
                                      @RequestParam Integer sum) throws NoSuchAlgorithmException {
        String paymentUrl = paymentService.deposit(user, sum);
        return new RedirectView(paymentUrl);
    }

    @GetMapping("/changeCredentials/{userId}/{currentUserId}/{code}")
    public String approveCredentials(@PathVariable Integer userId,
                                     @PathVariable Integer currentUserId,
                                     @PathVariable String code,
                                     Model model) throws WrongCredentialsException {

        userRegister.approveCredentials(userId, currentUserId, code);
        BookstoreUser user = (BookstoreUser) userRegister.getCurrentUser();
        model.addAttribute("curUsr", user);
        model.addAttribute("transactions", balanceTransactionService.findTransactionsByUser(user));
        model.addAttribute("part", null);
        model.addAttribute("credentialsSuccess", "Профиль успешно сохранен");
        return "/profile";
    }

}
