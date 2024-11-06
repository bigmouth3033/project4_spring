package com.service.main.repository;

import com.service.main.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {

    Property findByIdAndUserId(Integer id, Integer userId);


    @Query(value = "select p from Property p where p.id = :id")
    Property findListingById(@Param("id") int id);
}


