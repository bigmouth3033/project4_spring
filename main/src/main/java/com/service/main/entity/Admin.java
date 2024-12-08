package com.service.main.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true, nullable = false)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String address;

    private Date dob;

    private String phoneNumber;

    private String avatar;

    private Date createdAt = new Date();

    private Date updatedAt = new Date();

    private boolean status;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<AdminManageCity> adminManageCities;

    @OneToMany(mappedBy = "admin")
    @JsonManagedReference
    private List<AdminRole> adminRoles;
}
