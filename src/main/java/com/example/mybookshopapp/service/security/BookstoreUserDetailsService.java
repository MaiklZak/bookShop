package com.example.mybookshopapp.service.security;

import com.example.mybookshopapp.entity.security.PhoneNumberUserDetails;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.entity.security.BookstoreUserDetails;
import com.example.mybookshopapp.repository.security.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookstoreUserDetailsService implements UserDetailsService {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public BookstoreUserDetailsService(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        BookstoreUser bookstoreUser = bookstoreUserRepository.findBookstoreUserByEmail(s);

        if (bookstoreUser != null) {
            return new BookstoreUserDetails(bookstoreUser);
        }

        bookstoreUser = bookstoreUserRepository.findBookstoreUserByPhone(s);
        if (bookstoreUser != null) {
            return new PhoneNumberUserDetails(bookstoreUser);
        } else {
            throw new UsernameNotFoundException("user not found doh!!!");
        }
    }
}
