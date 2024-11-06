package com.service.main.repository;

import com.service.main.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Integer> {

    @Query(value = "select p from Badge p where p.type != 'host'")
    List<Badge> findUserBadge();

    @Query(value = "select p from Badge p where p.type = 'host'")
    List<Badge> findHostBadge();
}
