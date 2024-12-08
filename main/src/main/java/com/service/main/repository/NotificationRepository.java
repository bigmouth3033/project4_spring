package com.service.main.repository;

import com.service.main.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query(value = "select n from Notification n where n.user.id = :userId and n.isRead is false order by n.id desc limit 20")
    List<Notification> findUserPopUpNotification(@Param("userId") Integer userId);
}
