package com.service.main.repository;

import com.service.main.entity.AdminManageCity;
import com.service.main.entity.AdminManageCityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminManageCityRepository extends JpaRepository<AdminManageCity, AdminManageCityId> {
    @Modifying
    @Query(value = "delete from AdminManageCity a where a.admin.id = :userId and a.managedCity.id not in :cityId")
    void deleteManagedCity(@Param("userId") int userId, @Param("cityId") List<Integer> cityId);
}
