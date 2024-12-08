package com.service.main.dto;

import com.service.main.entity.UserDocumentImage;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;

    private String email;

    private String firstName ;

    private String lastName;

    private String address;

    private String phoneNumber;

    private String avatar;

    private Date dob;

}
