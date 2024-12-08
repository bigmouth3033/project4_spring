package com.service.main.repository;

import com.service.main.entity.ManagedCity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagedCityRepository extends JpaRepository<ManagedCity, Integer> {
    @Query(value = "select c from ManagedCity c where (c.cityName like %:cityName% or :cityName is null) and (c.isManaged = :status or :status is null)")
    Page<ManagedCity> findCity(@Param("cityName") String cityName, @Param("status") Boolean status, Pageable pageable);

    @Query(value = "select c from ManagedCity c")
    List<ManagedCity> findManagedCity();

}
