package com.service.main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminManageCity {
    @EmbeddedId
    private AdminManageCityId id;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @MapsId("adminId")
    @JsonBackReference
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "managed_city_id")
    @MapsId("managedCityId")
    @JsonBackReference
    private ManagedCity managedCity;
}

