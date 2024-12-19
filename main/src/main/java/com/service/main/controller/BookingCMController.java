package com.service.main.controller;

import com.service.main.dto.AcceptReservationBookingDto;
import com.service.main.dto.CustomPaging;
import com.service.main.dto.CustomResult;
import com.service.main.dto.PropertyBookingDto;
import com.service.main.service.customer.BookingCMService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.PUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bookingCM")
public class BookingCMController {

    @Autowired
    private BookingCMService bookingCMService;

    @GetMapping("bookings")
    public ResponseEntity<CustomResult> getBookings(@RequestParam String status){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var customResult = bookingCMService.getBookings(email, status);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("booking_count")
    public ResponseEntity<CustomResult> getBookingCount(){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var customResult = bookingCMService.getBookingCount(email);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("property_booking")
    public ResponseEntity<CustomResult> getPropertyBooking(@RequestParam int propertyId){
        var customResult = bookingCMService.getBookingOfProperty(propertyId);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("get_host_reserved_booking")
    public ResponseEntity<CustomPaging> getHostReservedBooking(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam String status, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate, @RequestParam(required = false) Integer propertyId){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var customPaging = bookingCMService.getReservedBooking(email,status,pageNumber,pageSize, startDate, endDate, propertyId);
        return ResponseEntity.ok(customPaging);
    }

    @GetMapping("get_booking_conflict_list")
    public ResponseEntity<CustomResult> getBookingConflictList(@RequestParam int bookingId){
        var customResult = bookingCMService.checkBookingConflict(bookingId);
        return ResponseEntity.ok(customResult);
    }

    @PutMapping("accept_reservation")
    public ResponseEntity<CustomResult> acceptReservation(@ModelAttribute AcceptReservationBookingDto acceptReservationBookingDto){
        var customResult = bookingCMService.acceptBooking(acceptReservationBookingDto);
        return ResponseEntity.ok(customResult);
    }

    @PutMapping("deny_reservation")
    public ResponseEntity<CustomResult> denyReservation(Integer bookingId){
        var customResult = bookingCMService.denyBooking(bookingId);
        return ResponseEntity.ok(customResult);
    }

    @PostMapping("add")
    public ResponseEntity<CustomResult> createBooking(@ModelAttribute PropertyBookingDto bookingDto) {
        var customResult = bookingCMService.createBooking(bookingDto);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("getBookingById")
    public ResponseEntity<CustomResult> getBooking(@RequestParam() Integer bookingId) {
        var customResult = bookingCMService.getBooking(bookingId);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("get_user_booking")
    @RolesAllowed({"USER"})
    public ResponseEntity<CustomPaging> getUserBooking(@RequestParam int pageNumber,
                                                       @RequestParam int pageSize,
                                                       @RequestParam String status,
                                                       @RequestParam String startDate,
                                                       @RequestParam String endDate) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var customPaging = bookingCMService.getUserTrips(email,pageNumber,pageSize, status, startDate, endDate);
        return ResponseEntity.ok(customPaging);
    }

    @GetMapping("get_tripping_count")
    @RolesAllowed({"USER"})
    public ResponseEntity<CustomResult> getTrippingCount(@RequestParam String startDate,
                                                         @RequestParam String endDate) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var customResult = bookingCMService.getTripCount(email, startDate, endDate);
        return ResponseEntity.ok(customResult);
    }


    @GetMapping("get_user_reservation")
    @RolesAllowed({"USER"})
    public ResponseEntity<CustomPaging> getUserReservation(@RequestParam int pageNumber,
                                                       @RequestParam int pageSize,
                                                       @RequestParam String status,
                                                       @RequestParam String startDate,
                                                       @RequestParam String endDate) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var customPaging = bookingCMService.getUserReservationTrips(email,pageNumber,pageSize, status, startDate, endDate);
        return ResponseEntity.ok(customPaging);
    }

    @GetMapping("get_reserved_count")
    @RolesAllowed({"USER"})
    public ResponseEntity<CustomResult> getReservedCount(@RequestParam String startDate,
                                                         @RequestParam String endDate) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var customResult = bookingCMService.getReservedCount(email, startDate, endDate);
        return ResponseEntity.ok(customResult);
    }

}
