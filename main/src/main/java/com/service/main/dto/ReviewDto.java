package com.service.main.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.service.main.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Integer id;

    private int totalScore;

    private Integer cleanlinessScore;

    private Integer accuracyScore;

    private Integer checkinScore;

    private Integer communicationScore;

    private UserAuthDto toUser;

    private String review;

    private Date createdAt;

    private Date updatedAt;

    private UserAuthDto user;

    private BookingDto booking;
}
