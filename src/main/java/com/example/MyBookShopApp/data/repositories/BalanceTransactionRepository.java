package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.model.BalanceTransaction;
import com.example.MyBookShopApp.security.BookstoreUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, Integer> {

    Page<BalanceTransaction> findBalanceTransactionByUser(BookstoreUser user, Pageable nextPage);
}
