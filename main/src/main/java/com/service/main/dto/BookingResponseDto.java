package com.service.main.dto;

import com.service.main.entity.BookDateDetail;
import com.service.main.entity.Property;
import com.service.main.entity.RefundPolicy;
import com.service.main.entity.Review;
import com.service.main.entity.Transaction;
import com.service.main.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class BookingResponseDto {
    private Integer id;
    private Date checkInDay;
    private Date checkOutDay;
    private int totalPerson;
    private int children;
    private int adult;
    private String bookingType;
    private String selfCheckInInstruction;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private String bookingCode;
    private double amount;

    private Property property;
    private RefundPolicy refundPolicy;
    private Review hostReview;
    private Review userReview;
    private User host;
    private User customer;
    private List<Transaction> transactions;
    private List<BookDateDetail> bookDateDetails;
}
