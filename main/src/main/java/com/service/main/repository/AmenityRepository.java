package com.service.main.repository;

import com.service.main.entity.Amenity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Integer> {

    @Query(value = "select a.type from Amenity a group by a.type")
    List<String> getTypeOfAmenity();

    @Query(value = "select p from Amenity p where (:search is null or p.name like %:search% or p.type like %:search%) and (:status is null or p.status = :status)")
    Page<Amenity> getAmenity(@Param("search") String search, @Param("status") Boolean status, Pageable pageable);
}
