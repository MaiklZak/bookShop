package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {

    SmsCode findByCode(String code);
}
