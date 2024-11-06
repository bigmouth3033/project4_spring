package com.service.main.repository;

import com.service.main.dto.CategoryDto;
import com.service.main.entity.PropertyCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;

@Repository
public interface PropertyCategoryRepository extends JpaRepository<PropertyCategory, Integer> {

    @Query(value = "select p from PropertyCategory p where (:search is null or p.categoryName like %:search%) and (:status is null or p.status = :status)")
    Page<PropertyCategory> getCategory(@Param("search") String search, @Param("status") Boolean status, Pageable pageable);

    @Query(value = "select new com.service.main.dto.CategoryDto(c.id, c.categoryName, c.description, c.categoryImage) from PropertyCategory c where c.status = true")
    List<CategoryDto> getAvailableCategory();
}
