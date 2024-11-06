package com.service.main.repository;

import com.service.main.entity.AdminManageCity;
import com.service.main.entity.AdminManageCityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminManageCityRepository extends JpaRepository<AdminManageCity, AdminManageCityId> {
}
