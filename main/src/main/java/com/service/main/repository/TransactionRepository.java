package com.service.main.repository;

import com.service.main.entity.Transaction;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE t.booking.id = :bookingId")
    List<Transaction> findByBookingId(@Param("bookingId") int bookingId);

}
