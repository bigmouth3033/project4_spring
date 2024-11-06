package com.service.main.repository;

import com.service.main.entity.RefundPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundPolicyRepository extends JpaRepository<RefundPolicy, Integer> {
}
