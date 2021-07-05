package com.example.MyBookShopApp.security.entity;

import com.example.MyBookShopApp.security.entity.BookstoreUser;
import com.example.MyBookShopApp.security.entity.BookstoreUserDetails;

public class PhoneNumberUserDetails extends BookstoreUserDetails {

    public PhoneNumberUserDetails(BookstoreUser bookstoreUser) {
        super(bookstoreUser);
    }

    @Override
    public String getUsername() {
        return getBookstoreUser().getPhone();
    }
}
