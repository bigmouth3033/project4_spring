package com.service.main.dto.userDto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class GovernmentRequest {
    private Integer IdType;
    private String governmentCountry;
    private MultipartFile frontImage;
    private MultipartFile backImage;
}
