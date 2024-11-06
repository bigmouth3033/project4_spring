package com.service.main.repository;

import com.service.main.entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyImageRepository extends JpaRepository<PropertyImage, Integer> {


    void deleteAllByPropertyId(Integer propertyId);
}
