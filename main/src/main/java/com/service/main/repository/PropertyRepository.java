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
}


