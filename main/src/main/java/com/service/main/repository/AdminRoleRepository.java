package com.service.main.repository;

import com.service.main.entity.AdminRole;
import com.service.main.entity.AdminRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRoleRepository extends JpaRepository<AdminRole, AdminRoleId> {
    @Modifying
    @Query(value = "delete from AdminRole a " +
            "where a.admin.id = :userId " +
            "and a.role.id not in :roleIds ")
    void deleteEmployeeRole(@Param("userId") int userId,
                            @Param("roleIds") List<Integer> roleIds);

}
