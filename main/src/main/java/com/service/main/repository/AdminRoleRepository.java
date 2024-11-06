package com.service.main.repository;

import com.service.main.entity.AdminRole;
import com.service.main.entity.AdminRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRoleRepository extends JpaRepository<AdminRole, AdminRoleId> {
}
