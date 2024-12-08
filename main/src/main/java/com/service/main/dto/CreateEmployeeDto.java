package com.service.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeDto {
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String address;

    private String dob;

    private String phoneNumber;

    private MultipartFile avatar;

    private List<Integer> cityIds;

    private List<Integer> roleIds;

 }
