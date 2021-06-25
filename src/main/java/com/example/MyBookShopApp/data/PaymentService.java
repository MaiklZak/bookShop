package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.model.BalanceTransaction;
import com.example.MyBookShopApp.data.model.Book;
import com.example.MyBookShopApp.data.model.BookUser;
import com.example.MyBookShopApp.data.model.BookUserType;
import com.example.MyBookShopApp.data.repositories.BalanceTransactionRepository;
import com.example.MyBookShopApp.data.repositories.BookRepository;
import com.example.MyBookShopApp.data.repositories.BookUserRepository;
import com.example.MyBookShopApp.errs.NoEnoughFundsForPayment;
import com.example.MyBookShopApp.security.BookstoreUser;
import com.example.MyBookShopApp.security.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class PaymentService {

    @Value("${robokassa.merchant.login}")
    private String merchantLogin;

    @Value("${robokassa.pass.first.test}")
    private String firstTestPass;

    private final BookRepository bookRepository;
    private final BookstoreUserRepository bookstoreUserRepository;
    private final BalanceTransactionRepository balanceTransactionRepository;
    private final BookUserRepository bookUserRepository;

    public PaymentService(BookRepository bookRepository, BookstoreUserRepository bookstoreUserRepository, BalanceTransactionRepository balanceTransactionRepository, BookUserRepository bookUserRepository) {
        this.bookRepository = bookRepository;
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.balanceTransactionRepository = balanceTransactionRepository;
        this.bookUserRepository = bookUserRepository;
    }

    public String getPaymentUrl(List<Book> booksFromCookieSlugs) throws NoSuchAlgorithmException {
        Double paymentSumTotal = booksFromCookieSlugs.stream().mapToDouble(Book::discountPrice).sum();
        MessageDigest md = MessageDigest.getInstance("MD5");
        String invId = "5"; //just for testing TODO order indexing later
        md.update((merchantLogin + ":" + paymentSumTotal.toString() + ":" + invId + ":" + firstTestPass).getBytes());
        return "https://auth.robokassa.ru/Merchant/index.aspx" +
                "?MerchantLogin=" + merchantLogin +
                "&InvId=" + invId +
                "&Culture=ru" +
                "&Encoding=utf-8" +
                "&OutSum=" + paymentSumTotal.toString() +
                "&SignatureValue=" + DatatypeConverter.printHexBinary(md.digest()).toUpperCase() +
                "&IsTest=1";
    }

    public String getPaymentUrl(Integer sum, Integer invId) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update((merchantLogin + ":" + sum + ":" + invId + ":" + firstTestPass).getBytes());
        return "https://auth.robokassa.ru/Merchant/index.aspx" +
                "?MerchantLogin=" + merchantLogin +
                "&InvId=" + invId +
                "&Culture=ru" +
                "&Encoding=utf-8" +
                "&OutSum=" + sum +
                "&SignatureValue=" + DatatypeConverter.printHexBinary(md.digest()).toUpperCase() +
                "&IsTest=1";
    }

    @Transactional(rollbackFor = NoEnoughFundsForPayment.class)
    public Boolean buyBooksByUser(BookstoreUser user) throws NoEnoughFundsForPayment {
        List<Book> bookListBuy = bookRepository.findBooksByUserAndType(user, BookUserType.CART);
        Integer sumBooks = bookListBuy.stream()
                .map(book -> balanceTransactionRepository.save(new BalanceTransaction(user,
                        book,
                        book.discountPrice(),
                        "Покупка книги: " + book.getTitle())))
                .map(BalanceTransaction::getValue).mapToInt(Integer::intValue).sum();
        if (user.getBalance() < sumBooks) {
            throw new NoEnoughFundsForPayment("No Enough Funds, make deposit to the account");
        }
        user.setBalance(user.getBalance() - sumBooks);
        bookstoreUserRepository.save(user);
        for (Book book : bookListBuy) {
            BookUser bookUser = bookUserRepository.findByBookAndUserAndType(book, user, BookUserType.CART);
            bookUser.setType(BookUserType.PAID);
            bookUserRepository.save(bookUser);
        }
        return true;
    }
}
