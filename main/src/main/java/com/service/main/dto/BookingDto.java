package com.service.main.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.service.main.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Integer id;

    private Date checkInDay;

    private Date checkOutDay;

    private int totalPerson;

    private int adult;

    private int children;

    private String bookingType;

    private String selfCheckInInstruction;

    private Date createdAt;

    private Date updatedAt;

    private String status;

    private Property property;

    private RefundPolicy refundPolicy;

    private Review hostReview;

    private Review userReview;

    private User host;

    private double amount;

    private String bookingCode;

    private User customer;

    private List<Transaction> transactions;

    private List<BookDateDetail> bookDateDetails;

}
