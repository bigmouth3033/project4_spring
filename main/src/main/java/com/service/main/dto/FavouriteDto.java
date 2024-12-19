package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteDto
{
    //Request DTO
    private Integer userId;
    private Integer propertyId;
    private String collectionName;

}
