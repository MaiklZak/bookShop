package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.PaymentService;
import com.example.MyBookShopApp.data.repositories.BookRepository;
import com.example.MyBookShopApp.data.BalanceTransactionService;
import com.example.MyBookShopApp.security.BookstoreUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.NoSuchAlgorithmException;

@Controller
public class ProfileController {

    private final BookRepository bookRepository;
    private final PaymentService paymentService;
    private final BalanceTransactionService balanceTransactionService;

    public ProfileController(BookRepository bookRepository, PaymentService paymentService, BalanceTransactionService balanceTransactionService) {
        this.bookRepository = bookRepository;
        this.paymentService = paymentService;
        this.balanceTransactionService = balanceTransactionService;
    }

    @PostMapping("/payment")
    public RedirectView handlePayment(@AuthenticationPrincipal BookstoreUserDetails user, @RequestParam Integer sum) throws NoSuchAlgorithmException {
        Integer transactionId = balanceTransactionService.newTransaction(user.getBookstoreUser(), sum, "Depositing funds through Robokassa");
        String paymentUrl = paymentService.getPaymentUrl(sum, transactionId);
        return new RedirectView(paymentUrl);
    }
}
