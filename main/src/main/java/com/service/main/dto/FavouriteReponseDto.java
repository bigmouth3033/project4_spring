package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteReponseDto {
    private Integer propertyId;
    private String collectionName;
    private String propertyName;
    private String propertyImage;
}
