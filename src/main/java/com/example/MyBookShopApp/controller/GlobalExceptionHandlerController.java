package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.errs.IncorrectAmountToEnterException;
import com.example.MyBookShopApp.errs.NoEnoughFundsForPayment;
import com.example.MyBookShopApp.errs.WrongCredentialsException;
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

    @ExceptionHandler(WrongCredentialsException.class)
    public String handlePasswordNoConfirmed(WrongCredentialsException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("credentialsError", e);
        return "redirect:/profile";
    }

    @ExceptionHandler(NoEnoughFundsForPayment.class)
    public String handleNoEnoughFundsForPayment(NoEnoughFundsForPayment e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("fundsError", e);
        return "redirect:/books/cart";
    }

    @ExceptionHandler(IncorrectAmountToEnterException.class)
    public String handleIncorrectAmountToEnter(IncorrectAmountToEnterException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("amountError", e);
        return "redirect:/profile?part=topupPart";
    }
}
