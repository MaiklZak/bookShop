package com.example.MyBookShopApp.errs;

public class WrongCredentialsException extends Exception {
    public WrongCredentialsException(String message) {
        super(message);
    }
}
