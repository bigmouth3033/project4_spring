package com.service.main.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String categoryName;

    private String description;

    private String categoryImage;

    private boolean status;

    @OneToMany(mappedBy = "propertyCategory", fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Property> properties;
}
