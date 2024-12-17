package com.service.main.repository;

import com.service.main.entity.Mail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Integer> {

    @Query("select m from Mail m where :now >= m.sendDate and m.isSend = false")
    List<Mail> getPendingMail(@Param("now") LocalDateTime now);


    @Query("select m from Mail m where m.subject like %:searchHeader% and (:status = 'All'  or (m.isSend = true and :status = 'send') or (m.isSend = false and :status = 'notsend'))")
    Page<Mail> getMailList(@Param("searchHeader") String searchHeader, @Param("status") String status, Pageable pageable);
}
