package com.service.main.repository;

import com.service.main.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    @Query(value = "select p from Property p where p.user.id = :userId and (p.propertyTitle like %:search% or (:search = '' and p.propertyTitle is null)) and (p.status = :status or :status = 'All')")
    Page<Property> findListingByUserId(@Param("userId") Integer userId, @Param("search") String search, @Param("status") String status,  Pageable pageable);


    List<Property> findListingByUserId(Integer userId);

    @Query(value = "select distinct p " +
            "from Property p " +
            "left join p.managedCity pc " +
            "left join p.propertyAmenities pa " +
            "where p.status != 'PROGRESS' " +
            "and (p.status = :status or :status = 'All') " +
            "and (:isAdmin = true or p.managedCity in :employeeManagedCity) " +
            "and (:propertySearchText is null or p.propertyTitle like %:propertySearchText%) " +
            "and (:searchHost is null or CONCAT(p.user.firstName, ' ', p.user.lastName) like %:searchHost% or p.user.email like %:searchHost%) " +
            "and (:bookType = 'All' or p.bookingType = :bookType) " +
            "and (:locationsIds is null or pc.id in :locationsIds) " +
            "and (:amenityIds is null or pa.amenity.id in :amenityIds ) " +
            "and (:categoryIds is null or p.propertyCategory.id in :categoryIds)")
    Page<Property> searchListingByAdmin(
            @Param("isAdmin") boolean isAdmin,
            @Param("employeeManagedCity") List<Integer> employeeManagedCity,
            @Param("status") String status,
            @Param("propertySearchText") String propertySearchText,
            @Param("searchHost") String searchHost,
            @Param("bookType") String bookType,
            @Param("locationsIds") List<Integer> locationsIds,
            @Param("amenityIds") List<Integer> amenityIds,
            @Param("categoryIds") List<Integer> categoryIds,
            Pageable pageable);
}


