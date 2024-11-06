package com.service.main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double amount;

    private String transactionType; // escrow, refund, webrevenue, hostrevenue

    private Date transferOn;

    @ManyToOne
    @JoinColumn(name = "bookingId")
    @JsonBackReference
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User user;
}
