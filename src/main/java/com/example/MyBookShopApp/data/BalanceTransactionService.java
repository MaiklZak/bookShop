package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.model.BalanceTransaction;
import com.example.MyBookShopApp.data.repositories.BalanceTransactionRepository;
import com.example.MyBookShopApp.security.BookstoreUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceTransactionService {

    private final BalanceTransactionRepository balanceTransactionRepository;

    @Autowired
    public BalanceTransactionService(BalanceTransactionRepository balanceTransactionRepository) {
        this.balanceTransactionRepository = balanceTransactionRepository;
    }

    public Integer newTransaction(BookstoreUser user, Integer sum, String description) {
        BalanceTransaction transaction = balanceTransactionRepository.save(new BalanceTransaction(user, null, sum, description));
        return transaction.getId();
    }

    public List<BalanceTransaction> findTransactionsByUser(BookstoreUser user) {
        return balanceTransactionRepository.findBalanceTransactionByUser(user);
    }
}
