package com.service.main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String propertyType;

    private String propertyTitle;

    private int maximumMonthPreBook;

    private String bookingType;

    private double basePrice;

    private int weeklyDiscount;

    private int monthlyDiscount;

    private String addressCode;

    private String addressDetail;

    private String checkInAfter;

    private String checkOutBefore;

    private int maximumGuest;

   private int numberOfBathRoom;

    private int numberOfBedRoom;

    private int numberOfBed;

    private boolean isPetAllowed;

    private boolean isSmokingAllowed;

    @Column(columnDefinition = "TEXT")
    private String additionalRules;

    private Integer maximumStay;

    private Integer minimumStay;

    @Column(columnDefinition = "TEXT")
    private String aboutProperty;

    @Column(columnDefinition = "TEXT")
    private String guestAccess;

    @Column(columnDefinition = "TEXT")
    private String detailToNote;

    private boolean isSelfCheckIn;

    private String selfCheckInType;

    private String coordinatesX;

    private String coordinatesY;

    @Column(columnDefinition = "TEXT")
    private String suggestion;

    private String status;

    private Date createdAt = new Date();

    private Date updatedAt = new Date();

    @ManyToOne
    @JoinColumn(name = "managedCityId")
    @JsonBackReference
    private ManagedCity managedCity;

    @ManyToOne
    @JoinColumn(name = "refundPolicyId")
    @JsonBackReference
    private RefundPolicy refundPolicy;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "propertyCategoryId")
    @JsonBackReference
    private PropertyCategory propertyCategory;

    @ManyToOne
    @JoinColumn(name = "instantBookRequirementId", nullable = true)
    private Badge instantBookRequirement;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PropertyImage> propertyImages;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PropertyNotAvailableDate> propertyNotAvailableDates;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PropertyExceptionDate> propertyExceptionDates;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PropertyAmenity> propertyAmenities;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Favourite> favourites;


    //them 5/12
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Booking> bookings;
}
