package com.example.MyBookShopApp.security;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TokenRevokerRepository extends CrudRepository<TokenRevoker, Integer> {
    TokenRevoker findByValue(String token);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM token_blacklist WHERE expiration < NOW()", nativeQuery = true)
    void deleteExpiredDate();
}
