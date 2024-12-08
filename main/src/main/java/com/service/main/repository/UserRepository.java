package com.service.main.repository;

import com.service.main.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByEmail(String email);

    @Query(value = "select u from User u where u.id != :userId and u.id not in :friendsId and (u.email like %:search% or CONCAT(u.firstName, ' ', u.lastName) like %:search% or :search is null ) order by u.id limit 20")
    List<User> searchChatUser(@Param("search") String search, @Param("userId") int userId, @Param("friendsId") List<Long> friendsId);

    @Query(value = "select u from User u where u.id != :userId and (u.email like %:search% or CONCAT(u.firstName, ' ', u.lastName) like %:search% or :search is null ) order by u.id limit 20")
    List<User> searchUserGroupChat(@Param("userId") int userId, @Param("search") String search);

    @Query(value = "select distinct u " +
            "from User u " +
            "left join u.userBadges ub " +
            "where (u.email like %:searchText% or CONCAT(u.firstName, ' ', u.lastName) like %:searchText%) " +
            "and (:badges is null or ub.badge.id in :badges) " +
            "and (:userType = 'all' or " +
            "    (:userType = 'host' and size(u.properties) > 0 and " +
            "        (select count(p) from Property p where p.user = u and p.status = 'PUBLIC') > 0) " +
            "    or (:userType = 'customer' and(size(u.properties) = 0 or (select count(p) from Property p where p.user = u and p.status = 'PUBLIC') = 0 )  )) ")
    Page<User> getUserByAdmin(@Param("searchText") String searchText,
                              @Param("badges") List<Integer> badges,
                              @Param("userType") String userType,
                              Pageable pageable);


}
