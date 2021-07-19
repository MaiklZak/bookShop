package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.google.api.books.Item;
import com.example.mybookshopapp.dto.google.api.books.Root;
import com.example.mybookshopapp.entity.*;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.entity.security.BookstoreUserDetails;
import com.example.mybookshopapp.errs.BookstoreApiWrongParameterException;
import com.example.mybookshopapp.repository.*;
import com.example.mybookshopapp.repository.security.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;
    private final BookUserRepository bookUserRepository;
    private final BookstoreUserRepository bookstoreUserRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final TagRepository tagRepository;

    @Autowired
    public BookService(BookRepository bookRepository, RestTemplate restTemplate, BookUserRepository bookUserRepository, BookstoreUserRepository bookstoreUserRepository, AuthorRepository authorRepository, GenreRepository genreRepository, TagRepository tagRepository) {
        this.bookRepository = bookRepository;
        this.restTemplate = restTemplate;
        this.bookUserRepository = bookUserRepository;
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.tagRepository = tagRepository;
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
            if (!data.isEmpty()) {
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

    /* returns a list of books where each book has genre or tag or author matches with user's books with type PAID,
       CART, KEPT or VIEWED in the order sorted by count of matches and date but without those books that user has,
       if this list is empty return books by rating and recency */
    public List<Book> getPageOfRecommendedBooksForUser(BookstoreUser user, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);

        removeBookStatusViewedForUserLongerThanMonth(user);

        List<Book> bookListByUser = bookRepository.findBooksByUserAndTypeIn(user,
                new BookUserType[]{BookUserType.CART, BookUserType.KEPT, BookUserType.PAID, BookUserType.VIEWED});

        List<Author> authorList = authorRepository.findByBookIn(bookListByUser);
        List<Genre> genreList = genreRepository.findByBooksIn(bookListByUser);
        List<Tag> tagList = tagRepository.findByBooksIn(bookListByUser);

        List<Book> recommendBooks = bookRepository.findBooksByAuthorInOrGenresInOrTagsInAndBookUserNotIn(
                authorList,
                genreList,
                tagList,
                user.getId(),
                nextPage).getContent();

        return recommendBooks.isEmpty() ? bookRepository.findRecommendedBooksSortRatingAndRecent(nextPage) : recommendBooks;
    }

    /* if cookie 'userHash' is not exists return books by rating and recency
           else retrieves user by hash and return list of recommended books for him */
    public List<Book> getPageOfRecommendedBooksForNotAuthenticatedUser(String userHash, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);

        if (userHash != null && !userHash.equals("")) {
            BookstoreUser user = bookstoreUserRepository.findBookstoreUserByHash(userHash);
            if (user != null) {
                return getPageOfRecommendedBooksForUser(user, offset, limit);
            }
        }
        return bookRepository.findRecommendedBooksSortRatingAndRecent(nextPage);

    }

    /* if cookie 'userHash' is not exists return popular books for everyone
           else retrieves user by hash and return list of popular books for him */
    public List<Book> getPageOfPopularBooksForNotAuthenticatedUser(String userHash, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        if (userHash != null && !userHash.equals("")) {
            BookstoreUser user = bookstoreUserRepository.findBookstoreUserByHash(userHash);
            if (user != null) {
                return getPageOfPopularBooksForUser(user, offset, limit);
            }
        }
        return bookRepository.findPopularBooks(nextPage);
    }

    /* return popular books for user considering recently viewed (less than a month) */
    public List<Book> getPageOfPopularBooksForUser(BookstoreUser user, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);

        removeBookStatusViewedForUserLongerThanMonth(user);

        return bookRepository.findPopularBooksForUser(user, nextPage);
    }

    public List<Book> getBookByTag(Integer offset, Integer limit, Integer id) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByTagId(id, nextPage).getContent();
    }

    public List<Book> getBooksByGenreId(Integer offset, Integer limit, Integer id) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByGenreId(id, nextPage);
    }

    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookByTitleContaining(searchWord, nextPage);
    }

    @Value("${google.books.api.key}")
    private String apiKey;

    public List<Book> getPageOfGoogleBooksApiSearchResult(String searchWord, Integer offset, Integer limit) {
        String requestUrl = "https://www.googleapis.com/books/v1/volumes" +
                "?q=" + searchWord +
                "&key=" + apiKey +
                "&filter=paid-ebooks" +      // filters incorrectly
                "&startIndex=" + offset +
                "&maxResults=" + limit;

        Root root = restTemplate.getForEntity(requestUrl, Root.class).getBody();
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

    /* deletes items from BookUser for user where type = VIEWED and time more then a month */
    public void removeBookStatusViewedForUserLongerThanMonth(BookstoreUser user) {
        bookUserRepository.deleteByUserAndTypeAndTimeBefore(user, BookUserType.VIEWED, LocalDateTime.now().minusMonths(1));
    }

    /* moves books from user retrieved by hash to authenticated user and delete user away with hash from cookie */
    public void moveBooksFromUserHashToCurrentUser(String userHash, BookstoreUserDetails user, HttpServletResponse response) {
        BookstoreUser bookstoreUserByHash = bookstoreUserRepository.findBookstoreUserByHash(userHash);
        if (userHash != null && !userHash.equals("") && bookstoreUserByHash != null) {
            List<Book> booksFromCookieUser = bookRepository.findBooksByUser(bookstoreUserByHash);
            for (Book book : booksFromCookieUser) {
                BookUser bookUserFromCookieUser = bookUserRepository.findByBookAndUser(book, bookstoreUserByHash);
                BookUser bookUserFromCurrentUser = bookUserRepository.findByBookAndUser(book, user.getBookstoreUser());
                if (bookUserFromCurrentUser != null) {
                    bookUserRepository.delete(bookUserFromCookieUser);
                } else {
                    bookUserFromCookieUser.setUser(user.getBookstoreUser());
                    bookUserRepository.save(bookUserFromCookieUser);
                }
            }
            bookstoreUserRepository.delete(bookstoreUserByHash);
            response.addCookie(new Cookie("userHash", ""));
        }
    }

    public List<Book> getPageOfViewedBooksByUser(BookstoreUser user, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        removeBookStatusViewedForUserLongerThanMonth(user);
        return bookRepository.findBooksByUserAndType(user, BookUserType.VIEWED, nextPage);
    }
}
