package com.service.main.repository;

import com.service.main.entity.PropertyExceptionDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PropertyExceptionDateRepository extends JpaRepository<PropertyExceptionDate, Integer> {


    PropertyExceptionDate findByPropertyIdAndDate(int propertyId, Date date);
}
