package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAmenityDto {
    private Integer id;

    private String name;

    private String description;

    private MultipartFile image;

    private String type;
}
