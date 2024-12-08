package com.service.main.dto;

import com.service.main.entity.Amenity;
import com.service.main.entity.PropertyCategory;
import com.service.main.entity.RefundPolicy;
import com.service.main.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyAdminDto {
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

    private String additionalRules;

    private Integer maximumStay;

    private Integer minimumStay;

    private String aboutProperty;

    private String guestAccess;

    private String detailToNote;

    private boolean isSelfCheckIn;

    private String selfCheckInType;

    private String coordinatesX;

    private String coordinatesY;

    private String status;

    private Integer managedCityId;

    private RefundDto refund;

    private CategoryDto propertyCategory;

    private Integer instantBookRequirementID;

    private List<String> propertyImages;

    private List<AmenityDto> propertyAmenities;

    private UserDto user;

    private Date createdAt;
}
