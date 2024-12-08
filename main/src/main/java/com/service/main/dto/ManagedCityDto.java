package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagedCityDto {
    private Integer id;

    private String cityName;

    private boolean isManaged;

    private Integer propertyCount;
}
