package com.service.main.repository;

import com.service.main.entity.PropertyNotAvailableDate;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyNotAvailableDateRepository extends JpaRepository<PropertyNotAvailableDate, Integer> {

    PropertyNotAvailableDate findByPropertyIdAndDate(int propertyId, Date time);

    @Query("SELECT p FROM PropertyNotAvailableDate p WHERE p.property.id = :propertyId")
    Optional<List<PropertyNotAvailableDate>> findByPropertyId(@Param("propertyId") Integer propertyId);
}
