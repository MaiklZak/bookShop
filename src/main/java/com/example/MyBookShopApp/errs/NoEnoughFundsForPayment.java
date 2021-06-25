package com.example.MyBookShopApp.errs;

public class NoEnoughFundsForPayment extends Exception {
    public NoEnoughFundsForPayment(String message) {
        super(message);
    }
}
