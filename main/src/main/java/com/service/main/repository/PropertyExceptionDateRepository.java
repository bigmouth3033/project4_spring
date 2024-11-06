package com.service.main.repository;

import com.service.main.entity.PropertyExceptionDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyExceptionDateRepository extends JpaRepository<PropertyExceptionDate, Integer> {
}
