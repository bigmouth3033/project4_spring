package com.service.main.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminManageCityId implements Serializable {
    private Integer adminId;
    private Integer managedCityId;
}
