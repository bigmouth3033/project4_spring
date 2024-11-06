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
public class AdminRole {
    @EmbeddedId
    private AdminRoleId adminRoleId;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @MapsId("adminId")
    @JsonBackReference
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @MapsId("roleId")
    @JsonBackReference
    private Role role;

}

