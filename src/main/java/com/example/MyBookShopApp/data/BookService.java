package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.model.Book;
import com.example.MyBookShopApp.data.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorFirstNameContaining(authorName);
    }

    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findBooksByTitleContaining(title);
    }

    public List<Book> getBooksWithPriceBetween(Integer min, Integer max) {
        return bookRepository.findBooksByPriceOldBetween(min, max);
    }

    public List<Book> getBooksWithPrice(Integer price) {
        return bookRepository.findBooksByPriceOldIs(price);
    }

    public List<Book> getBooksWithMaxPrice() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<Book> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookByTitleContaining(searchWord, nextPage);
    }

    public Page<Book> getPageOfRecentBooks(int offset, int limit) {
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "pubDate", "title"));
        return bookRepository.findAll(nextPage);
    }
    public List<Book> getPageOfRecentBooks(java.util.Date from, java.util.Date to, int offset, int limit) {
        if (from == null && to == null) {
            return getPageOfRecentBooks(offset, limit).getContent();
        }
        Pageable nextPage = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "pubDate", "title"));
        if (from == null) {
            return bookRepository.findBooksByPubDateBefore(new java.sql.Date(to.getTime()), nextPage);
        }
        if (to == null) {
            return bookRepository.findBooksByPubDateAfter(new java.sql.Date(from.getTime()), nextPage);
        }
        return bookRepository.findBooksByPubDateAfterAndPubDateBefore(new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime()), nextPage);
    }


    public List<Book> getPageOfPopularBooks(int offset, int limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findPopularBooks(nextPage);
    }

    public List<Book> getBookByTag(Integer offset, Integer limit, Integer id) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByTagId(id, nextPage).getContent();
    }

    public List<Book> getBooksByGenreId(Integer offset, Integer limit, Integer id) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByGenreId(id, nextPage);
    }
}
