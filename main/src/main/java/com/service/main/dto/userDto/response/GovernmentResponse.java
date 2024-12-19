package com.service.main.dto.userDto.response;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class GovernmentResponse {
    private Integer IdType;
    private String governmentCountry;
    private String frontImageUrl;
    private String backImageUrl;
}
