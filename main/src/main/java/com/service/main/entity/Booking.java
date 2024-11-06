package com.service.main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    private Date checkInDay;

    private Date checkOutDay;

    private int totalPerson;

    private String bookingType;

    private String selfCheckInInstruction;

    private Date createdAt = new Date();

    private Date updatedAt = new Date();

    private String status;

    @ManyToOne
    @JoinColumn(name = "propertyId")
    @JsonBackReference
    private Property property;

    @ManyToOne
    @JoinColumn(name = "refundPolicyId")
    @JsonBackReference
    private RefundPolicy refundPolicy;

    @OneToOne
    @JoinColumn(name = "hostReviewId")
    @JsonBackReference
    private Review hostReview;

    @OneToOne
    @JoinColumn(name = "userReviewId")
    @JsonBackReference
    private Review userReview;

    @ManyToOne
    @JoinColumn(name = "hostId")
    @JsonBackReference
    private User host;

    @ManyToOne
    @JoinColumn(name = "customerId")
    @JsonBackReference
    private User customer;

    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Transaction> transactions;


}