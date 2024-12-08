package com.service.main.repository;

import com.service.main.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByEmail(String email);

    Admin findByPhoneNumber(String phoneNumber);

    @Query(value =
            "select distinct  a from Admin a " +
            "left join a.adminRoles ar " +
            "left join a.adminManageCities ac " +
            "where (a.status = :status or :status is null) " +
            "and (a.email like %:searchText% or CONCAT(a.firstName, ' ', a.lastName) like %:searchText% or :searchText is null ) " +
            "and ar.role.roleName != 'ADMIN' " +
            "and (:roleFilter is null or ar.role.id in :roleFilter) " +
            "and (:cityFilter is null or ac.managedCity.id in :cityFilter )")
    Page<Admin> findEmployee(@Param("status") Boolean status,
                             @Param("searchText") String searchText,
                             @Param("cityFilter") List<Integer> cityFilter,
                             @Param("roleFilter") List<Integer> roleFilter,
                             Pageable pageable);
}
