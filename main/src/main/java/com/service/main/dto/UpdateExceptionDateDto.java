package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateExceptionDateDto {
    private int propertyId;

    private String start;

    private String end;

    private double price;
}
