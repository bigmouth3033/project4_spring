package com.service.main.repository;

import com.service.main.entity.PropertyNotAvailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyNotAvailableDateRepository extends JpaRepository<PropertyNotAvailableDate, Integer> {
}
