package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.model.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {

    SmsCode findByCode(String code);
}
