package com.example.MyBookShopApp.security.entity;

public class PhoneNumberUserDetails extends BookstoreUserDetails {

    public PhoneNumberUserDetails(BookstoreUser bookstoreUser) {
        super(bookstoreUser);
    }

    @Override
    public String getUsername() {
        return getBookstoreUser().getPhone();
    }
}
