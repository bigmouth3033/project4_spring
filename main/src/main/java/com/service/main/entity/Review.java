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
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    private int totalScore;

    private Integer cleanlinessScore;

    private Integer accuracyScore;

    private Integer checkinScore;

    private Integer communicationScore;

    private Integer toUser;

    private String review;

    private Date createdAt = new Date();

    private Date updatedAt = new Date();

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User user;


}
