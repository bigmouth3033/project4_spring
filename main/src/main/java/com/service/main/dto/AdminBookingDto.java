package com.service.main.dto;

import com.service.main.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminBookingDto {
    private Integer id;

    private Date checkInDay;

    private Date checkOutDay;

    private int totalPerson;

    private int children;

    private int adult;

    private String bookingType;

    private String selfCheckInInstruction;

    private Date createdAt;

    private Date updatedAt;

    private String status;

    private String propertyImage;

    private String propertyName;

    private String propertyCity;

    private RefundPolicy refundPolicy;

    private Review hostReview;

    private Review userReview;

    private User host;

    private User customer;

    private List<Transaction> transactions;

    private List<BookDateDetail> bookDateDetails;
}
