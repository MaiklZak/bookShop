package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.model.BalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, Integer> {
}
