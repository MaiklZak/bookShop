package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.dto.google.api.books.Item;
import com.example.MyBookShopApp.dto.google.api.books.Root;
import com.example.MyBookShopApp.entity.Author;
import com.example.MyBookShopApp.entity.Book;
import com.example.MyBookShopApp.entity.BookUser;
import com.example.MyBookShopApp.entity.BookUserType;
import com.example.MyBookShopApp.repository.BookRepository;
import com.example.MyBookShopApp.repository.BookUserRepository;
import com.example.MyBookShopApp.errs.BookstoreApiWrongParameterException;
import com.example.MyBookShopApp.security.entity.BookstoreUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;
    private final BookUserRepository bookUserRepository;

    @Autowired
    public BookService(BookRepository bookRepository, RestTemplate restTemplate, BookUserRepository bookUserRepository) {
        this.bookRepository = bookRepository;
        this.restTemplate = restTemplate;
        this.bookUserRepository = bookUserRepository;
    }

    public List<Book> getBookData() {
        return bookRepository.findAll();
    }

    //NEW BOOK SERVICE METHOD

    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorFirstNameContaining(authorName);
    }

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

    @Value("${google.books.api.key}")
    private String apiKey;

    public List<Book> getPageOfGoogleBooksApiSearchResult(String searchWord, Integer offset, Integer limit) {
        String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes" +
                "?q=" + searchWord +
                "&key=" + apiKey +
                "&filter=paid-ebooks" +      // filters incorrectly
                "&startIndex=" + offset +
                "&maxResults=" + limit;

        Root root = restTemplate.getForEntity(REQUEST_URL, Root.class).getBody();
        ArrayList<Book> list = new ArrayList<>();
        if (root != null) {
            for (Item item : root.getItems()) {
                Book book = new Book();
                if (item.getVolumeInfo() != null) {
                    book.setAuthor(new Author(item.getVolumeInfo().getAuthors()));
                    book.setTitle(item.getVolumeInfo().getTitle());
                    book.setImage(item.getVolumeInfo().getImageLinks().getThumbnail());
                }
                if (item.getSaleInfo() != null && item.getSaleInfo().getRetailPrice() != null) { // without (item.getSaleInfo().getRetailPrice() != null) throw exception
                    book.setPrice(item.getSaleInfo().getRetailPrice().getAmount());
                    double oldPrice = item.getSaleInfo().getListPrice().getAmount();
                    book.setPriceOld((int) oldPrice);
                }
                list.add(book);
            }
        }
        return list;
    }

    public void changeBookStatusToCartForUser(BookUserType type, String slug, BookstoreUser user) {
        Book book = bookRepository.findBookBySlug(slug);
        BookUser bookUser = bookUserRepository.findByBookAndUser(book, user);
        if (bookUser == null) {
            bookUserRepository.save(new BookUser(type, book, user));
        } else if (bookUser.getType().equals(BookUserType.VIEWED)) {
            bookUser.setType(BookUserType.CART);
            bookUserRepository.save(bookUser);
        }
    }

    public void removeBookFromCartBySlag(BookstoreUser user, String slug) {
        Book book = bookRepository.findBookBySlug(slug);
        BookUser bookUser = bookUserRepository.findByBookAndUserAndType(book, user, BookUserType.CART);
        bookUser.setType(BookUserType.VIEWED);
        bookUserRepository.save(bookUser);
    }

    public boolean changeBookStatusForUser(Book book, BookstoreUser user, BookUserType type) {
        BookUser bookUser = bookUserRepository.findByBookAndUser(book, user);
        if (bookUser == null) {
            bookUser = new BookUser(type, book, user);
            bookUserRepository.save(bookUser);
            return true;
        }
        return false;
    }
}
