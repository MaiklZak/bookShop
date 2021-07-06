package com.example.mybookshopapp.dto;

import com.example.mybookshopapp.entity.BalanceTransaction;

import java.util.List;

public class BalanceTransactionDto {

    private Integer count;
    private List<BalanceTransaction> transactions;

    public BalanceTransactionDto(List<BalanceTransaction> transactions) {
        this.transactions = transactions;
        this.count = transactions.size();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<BalanceTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BalanceTransaction> transactions) {
        this.transactions = transactions;
    }
}