package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.errs.NoEnoughFundsForPayment;
import com.example.MyBookShopApp.errs.PasswordNoConfirmed;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(EmptySearchException e, RedirectAttributes redirectAttributes) {
        Logger.getLogger(this.getClass().getSimpleName()).warning(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/";
    }

    @ExceptionHandler(PasswordNoConfirmed.class)
    public String handlePasswordNoConfirmed(PasswordNoConfirmed e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("passwordError", e);
        return "redirect:/profile";
    }

    @ExceptionHandler(NoEnoughFundsForPayment.class)
    public String handleNoEnoughFundsForPayment(NoEnoughFundsForPayment e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("fundsError", e);
        return "redirect:/books/cart";
    }
}
