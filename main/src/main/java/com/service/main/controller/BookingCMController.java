package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.service.customer.BookingCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
