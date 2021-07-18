package com.example.mybookshopapp.controller;

import com.example.mybookshopapp.errs.EmptySearchException;
import com.example.mybookshopapp.errs.IncorrectAmountToEnterException;
import com.example.mybookshopapp.errs.NoEnoughFundsForPayment;
import com.example.mybookshopapp.errs.WrongCredentialsException;
import com.example.mybookshopapp.errs.security.NotFoundUserWithContactException;
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

    @ExceptionHandler(NotFoundUserWithContactException.class)
    public String handleNotFoundUserWithContactException(NotFoundUserWithContactException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("authError", e);
        return "redirect:/signin";
    }
}
