package com.service.main.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private Integer id;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private Date dob;

    private String phoneNumber;

    private String avatar;

    private Date createdAt = new Date();

    private Date updatedAt = new Date();

    private boolean status;

    private List<RoleDto> roles;

    private List<ManagedCityDto> cities;
}
