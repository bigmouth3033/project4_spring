package com.service.main.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDateDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "bookingId")
    @JsonBackReference
    private Booking booking;

    private Date night;

    private double price;

    @ManyToOne
    @JoinColumn(name = "propertyId")
    @JsonBackReference
    private Property property;
}
