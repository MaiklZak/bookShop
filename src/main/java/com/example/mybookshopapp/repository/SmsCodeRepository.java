package com.example.mybookshopapp.repository;

import com.example.mybookshopapp.entity.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {

    SmsCode findByCode(String code);
}
