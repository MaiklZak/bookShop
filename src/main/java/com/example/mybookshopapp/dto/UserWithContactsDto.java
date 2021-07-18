package com.example.mybookshopapp.dto;

public class UserWithContactsDto {

    private String hash;

    private Integer balance;

    private String name;

    private String email;

    private String phone;

    public UserWithContactsDto(String hash, Integer balance, String name, String email, String phone) {
        this.hash = hash;
        this.balance = balance;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
