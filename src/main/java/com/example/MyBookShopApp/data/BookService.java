package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.aspects.annotations.LoggingBookServiceGetMethods;
import com.example.MyBookShopApp.aspects.annotations.LoggingBookServiceGetMethodsWithArg;
import com.example.MyBookShopApp.data.model.Book;
import com.example.MyBookShopApp.data.repositories.BookRepository;
import com.example.MyBookShopApp.errs.BookstoreApiWrongParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBookData() {
        return bookRepository.findAll();
    }

    //NEW BOOK SERVICE METHOD

    @LoggingBookServiceGetMethodsWithArg
    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorFirstNameContaining(authorName);
    }

    @LoggingBookServiceGetMethodsWithArg
    public List<Book> getBooksByTitle(String title) throws BookstoreApiWrongParameterException {
        if (title.equals("") || title.length() <= 1) {
            throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
        } else {
            List<Book> data = bookRepository.findBooksByTitleContaining(title);
            if (data.size() > 0) {
                return data;
            } else {
                throw new BookstoreApiWrongParameterException("No data found with specified parameters...");
            }
        }

    }

    @LoggingBookServiceGetMethodsWithArg
    public List<Book> getBooksWithPriceBetween(Integer min, Integer max) {
        return bookRepository.findBooksByPriceOldBetween(min, max);
    }

    @LoggingBookServiceGetMethodsWithArg
    public List<Book> getBooksWithPrice(Integer price) {
        return bookRepository.findBooksByPriceOldIs(price);
    }

    @LoggingBookServiceGetMethods
    public List<Book> getBooksWithMaxPrice() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    @LoggingBookServiceGetMethods
    public List<Book> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    @LoggingBookServiceGetMethods
    public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    @LoggingBookServiceGetMethods
    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookByTitleContaining(searchWord, nextPage);
    }
}
