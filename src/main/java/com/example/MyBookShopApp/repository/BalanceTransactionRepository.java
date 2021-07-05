package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.BalanceTransaction;
import com.example.MyBookShopApp.security.entity.BookstoreUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, Integer> {

    Page<BalanceTransaction> findBalanceTransactionByUser(BookstoreUser user, Pageable nextPage);
}
