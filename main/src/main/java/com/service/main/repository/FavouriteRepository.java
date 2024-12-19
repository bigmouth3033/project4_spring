package com.service.main.repository;

import com.service.main.dto.FavouriteIDDto;
import com.service.main.entity.Favourite;
import com.service.main.entity.FavouriteId;
import com.service.main.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, FavouriteId> {
    @Query("select case when count(c)> 0 then true else false end from Favourite c where lower(c.collectionName) like lower(:collectionName)")
    boolean existsCollectionName(@Param("collectionName") String collectionName);


    @Query(value = "select f from Favourite f " +
            "JOIN f.user u " +
            " where u.id = :userId")
    List<Favourite> getAllFavouriteByUserId(@Param("userId") Integer userId);

    boolean existsById(FavouriteId id);

    // Phương thức tìm kiếm mục yêu thích theo ID
    Favourite findFavouriteById(FavouriteId id);
}
