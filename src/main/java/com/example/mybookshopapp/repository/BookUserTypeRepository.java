package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.BookUserType;
import com.example.mybookshopapp.entity.TypeBookToUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookUserTypeRepository extends JpaRepository<BookUserType, Integer> {

    BookUserType findByCode(TypeBookToUser typeBookToUser);
}
