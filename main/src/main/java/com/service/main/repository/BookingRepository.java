package com.service.main.repository;

import com.service.main.dto.BookingCountDto;
import com.service.main.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {


    @Query(value = "select p from Booking p where p.host.id = :id and :date >= p.checkInDay and :date <= p.checkOutDay and p.status = 'ACCEPT' order by p.checkInDay")
    List<Booking> getCurrentlyHostingBook(@Param("id") Integer id,@Param("date") Date date);

    @Query(value = "select p from Booking p where p.host.id = :id and function('DATE', p.checkOutDay) = function('DATE', :date) and p.status = 'ACCEPT' order by p.checkInDay")
    List<Booking> getCheckoutHostingBook(@Param("id") Integer id,@Param("date") Date date );

    @Query(value = "select p from Booking p where p.host.id = :id and function('DATE', p.checkInDay) = function('DATE', :date) and p.status = 'ACCEPT' order by p.checkInDay")
    List<Booking> getCheckInHostingBook(@Param("id") Integer id,@Param("date") Date date );

    @Query(value = "select p from Booking p where p.host.id = :id and :date < p.checkInDay and p.status = 'ACCEPT' order by p.checkInDay")
    List<Booking> getUpcomingHostingBook(@Param("id") Integer id,@Param("date") Date date );

    @Query(value = "select p from Booking p where p.host.id = :id and :date > p.checkOutDay and p.hostReview is null and p.status = 'ACCEPT' order by p.checkInDay")
    List<Booking> getPendingReviewHostingBook(@Param("id") Integer id,@Param("date") Date date );


    @Query("select new com.service.main.dto.BookingCountDto(" +
            "(select count(p) from Booking p where p.host.id = :id and :date >= p.checkInDay and :date <= p.checkOutDay and p.status = 'ACCEPT'), " +
            "(select count(p) from Booking p where p.host.id = :id and function('DATE', p.checkOutDay) = function('DATE', :date) and p.status = 'ACCEPT'), " +
            "(select count(p) from Booking p where p.host.id = :id and function('DATE', p.checkInDay) = function('DATE', :date) and p.status = 'ACCEPT'), " +
            "(select count(p) from Booking p where p.host.id = :id and :date < p.checkInDay and p.status = 'ACCEPT'), " +
            "(select count(p) from Booking p where p.host.id = :id and :date > p.checkOutDay and p.hostReview is null and p.status = 'ACCEPT')) " +
            "from Booking p " +
            "where p.host.id = :id " +
            "group by p.host.id")
    BookingCountDto getBookingCounts(@Param("id") Integer id, @Param("date") Date date);

    @Query(value = "select b from Booking b where b.property.id = :propertyId and b.status = 'ACCEPT' ")
    List<Booking> findAllByPropertyId(@Param("propertyId") int propertyId);
}
