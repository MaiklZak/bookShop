package com.example.mybookshopapp.service;

import com.example.mybookshopapp.dto.google.api.books.Item;
import com.example.mybookshopapp.dto.google.api.books.Root;
import com.example.mybookshopapp.entity.*;
import com.example.mybookshopapp.entity.security.BookstoreUser;
import com.example.mybookshopapp.errs.BookstoreApiWrongParameterException;
import com.example.mybookshopapp.repository.*;
import com.example.mybookshopapp.repository.security.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;
    private final BookstoreUserRepository bookstoreUserRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final TagRepository tagRepository;
    private final BookUserService bookUserService;

    @Autowired
    public BookService(BookRepository bookRepository, RestTemplate restTemplate, BookstoreUserRepository bookstoreUserRepository, AuthorRepository authorRepository, GenreRepository genreRepository, TagRepository tagRepository, BookUserService bookUserService) {
        this.bookRepository = bookRepository;
        this.restTemplate = restTemplate;
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.tagRepository = tagRepository;
        this.bookUserService = bookUserService;
    }

    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorNameContaining(authorName);
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

    public List<Book> getBooksWithMaxPrice() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<Book> getBestsellers() {
        return bookRepository.getBestsellers();
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
            return bookRepository.findBooksByPubDateLessThanEqual(new java.sql.Date(to.getTime()), nextPage);
        }
        if (to == null) {
            return bookRepository.findBooksByPubDateGreaterThanEqual(new java.sql.Date(from.getTime()), nextPage);
        }
        return bookRepository.findBooksByPubDateGreaterThanEqualAndPubDateLessThanEqual(new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime()), nextPage);
    }


    /* returns a list of books where each book has genre or tag or author matches with user's books with typeBookToUser PAID,
       CART, KEPT or VIEWED in the order sorted by count of matches and date but without those books that user has,
       if this list is empty return books by rating and recency */
    public List<Book> getPageOfRecommendedBooksForUser(BookstoreUser user, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);

        bookUserService.removeBookStatusViewedForUserLongerThanMonth(user);

        List<Book> bookListByUser = bookRepository.findBooksByUserAndTypeIn(user,
                new TypeBookToUser[]{TypeBookToUser.CART, TypeBookToUser.KEPT, TypeBookToUser.PAID, TypeBookToUser.VIEWED});

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

        bookUserService.removeBookStatusViewedForUserLongerThanMonth(user);

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

    public List<Book> getPageOfViewedBooksByUser(BookstoreUser user, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        bookUserService.removeBookStatusViewedForUserLongerThanMonth(user);
        return bookRepository.findBooksByUserAndType(user, TypeBookToUser.VIEWED, nextPage);
    }

    public List<Book> getPaidBooksForUser(BookstoreUser user) {
        return bookRepository.findBooksByUserAndType(user, TypeBookToUser.PAID);
    }

    public List<Book> getArchivedBooksForUser(BookstoreUser user) {
        return bookRepository.findBooksByUserAndType(user, TypeBookToUser.ARCHIVED);
    }
}
