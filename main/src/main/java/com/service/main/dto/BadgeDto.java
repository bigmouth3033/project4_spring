package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BadgeDto {
    private Integer id;

    private String name;

    private String description;

    private String image;

    private String type;

    private String criteria;
}
