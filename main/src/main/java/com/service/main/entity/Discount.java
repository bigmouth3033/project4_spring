package com.service.main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String code;

    private boolean isPrivate;

    private int quantity;

    private int discountPercentage;

    private double maximumDiscount;

    private Date expiredDate;

    private Date startDate;

    private String status;

    private Date createdAt;

    private Date updatedAt;


    @PrePersist
    void onCreate(){
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @ManyToOne
    @JoinColumn(name = "propertyId")
    @JsonBackReference
    private Property property;
}
