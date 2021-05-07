package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findUserByBooksTitle(String title);
}
