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
public class PropertyAmenity {
    @EmbeddedId
    private PropertyAmenityId id;

    @ManyToOne
    @JoinColumn(name = "property_id")
    @MapsId("propertyId")
    @JsonBackReference
    private Property property;

    @ManyToOne
    @JoinColumn(name = "amenity_id")
    @MapsId("amenityId")
    @JsonBackReference
    private Amenity amenity;


}

