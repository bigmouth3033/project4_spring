package com.service.main.repository;

import com.service.main.entity.UserDocumentImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDocumentImageRepository extends JpaRepository<UserDocumentImage, Integer> {
}
