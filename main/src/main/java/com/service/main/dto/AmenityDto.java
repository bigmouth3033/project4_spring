package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmenityDto {
    private Integer id;

    private String name;

    private String description;

    private String image;

    private String type;

    private boolean status;
}
