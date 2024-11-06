package com.service.main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Favourite {

    @EmbeddedId
    private FavouriteId id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "property_id")
    @MapsId("propertyId")
    @JsonBackReference
    private Property property;

    private String collectionName;

    private Date createdAt = new Date();

    private Date updatedAt = new Date();

}

