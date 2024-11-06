package com.service.main.repository;

import com.service.main.entity.PropertyAmenity;
import com.service.main.entity.PropertyAmenityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PropertyAmenityRepository extends JpaRepository<PropertyAmenity, PropertyAmenityId> {


    void deleteAllByPropertyId(Integer propertyId);
}
