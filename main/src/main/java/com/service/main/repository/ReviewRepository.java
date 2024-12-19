package com.service.main.repository;

import com.service.main.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query(value = "select r from Review r where r.user.id = :id")
    Page<Review> getUserOwnReview(@Param("id") Integer id, Pageable pageable);

    @Query(value = "select r from Review r where r.toUser = :id")
    Page<Review> getUserReviewedByOther(@Param("id") Integer id, Pageable pageable);
}
