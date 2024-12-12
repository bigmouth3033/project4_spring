package com.service.main.repository;

import com.service.main.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {

    Property findByIdAndUserId(Integer id, Integer userId);


    @Query(value = "select p from Property p where p.id = :id")
    Property findListingById(@Param("id") int id);

    @Query(value = "SELECT p " +
            "FROM Property p " +
            "JOIN p.propertyAmenities pa " +
            "WHERE (p.propertyCategory.id = :categoryId OR :categoryId IS NULL) " +
            "AND (pa.amenity.id IN :amenities OR :amenities IS NULL) " + // Sử dụng IN để kiểm tra amenities
            "AND (p.propertyType = :propertyType OR :propertyType IS NULL) " +
            "AND (p.bookingType = :isInstant OR :isInstant IS NULL) " +
            "AND (p.isSelfCheckIn = :isSelfCheckIn OR :isSelfCheckIn IS NULL) " +
            "AND (p.isPetAllowed = :isPetAllowed OR :isPetAllowed IS NULL) " +
            "AND (:minPrice <= p.basePrice AND p.basePrice <= :maxPrice) " +
            "AND (p.numberOfBedRoom >= :room OR :room IS NULL) " +
            "AND (p.numberOfBed >= :bed OR :bed IS NULL) " +
            "AND (p.numberOfBathRoom >= :bathRoom OR :bathRoom IS NULL)")
    List<Property> findPropertiesWithSearchAndFilter(
            @Param("categoryId") Integer categoryId,
            @Param("propertyType") String propertyType,
            @Param("amenities") List<Integer> amenities, // Thay đổi kiểu dữ liệu về Integer
            @Param("isInstant") String isInstant,
            @Param("isSelfCheckIn") Boolean isSelfCheckIn,
            @Param("isPetAllowed") Boolean isPetAllowed,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("room") Integer room,
            @Param("bed") Integer bed,
            @Param("bathRoom") Integer bathRoom);
}


