package com.service.main.repository;

import com.service.main.entity.AuthenticationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface AuthenticationCodeRepository extends JpaRepository<AuthenticationCode, Integer> {
    AuthenticationCode findByCodeAndEmail(String code, String email);

    @Modifying
    @Query("delete from AuthenticationCode a where a.expiredTime < :date")
    void deleteExpiredCode(@Param("date") Date date);
}
