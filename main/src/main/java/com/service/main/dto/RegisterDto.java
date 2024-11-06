package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    private String code;

    private String email;

    private String password;

    private String dob;

    private String firstName;

    private String lastName;
}
