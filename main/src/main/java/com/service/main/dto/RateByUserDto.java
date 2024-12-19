package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateByUserDto {

    private Integer bookingId;

    private int totalScore;

    private Integer cleanlinessScore;

    private Integer accuracyScore;

    private Integer checkinScore;

    private Integer communicationScore;

    private String review;

    private Integer toUser;
}
