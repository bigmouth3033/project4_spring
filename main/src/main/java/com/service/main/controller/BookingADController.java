package com.service.main.controller;

import com.service.main.dto.CustomPaging;
import com.service.main.service.admin.BookingADService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("bookingAD")
public class BookingADController {

    @Autowired
    private BookingADService bookingADService;


    @GetMapping("get_admin_booking")
    @RolesAllowed({"ADMIN", "BOOKING_MANAGEMENT"})
    public ResponseEntity<CustomPaging> getAdminBooking(@RequestParam("pageNumber") int pageNumber,
                                                        @RequestParam("pageSize") int pageSize,
                                                        @RequestParam(value = "hostSearch", required = false, defaultValue = "") String hostSearch,
                                                        @RequestParam(value = "customerSearch", required = false, defaultValue = "") String customerSearch,
                                                        @RequestParam("bookingType") String bookingType,
                                                        @RequestParam("startDate") String startDate,
                                                        @RequestParam("endDate") String endDate,
                                                        @RequestParam(value = "locationIds", required = false) List<Integer> locationIds,
                                                        @RequestParam("status") String status,
                                                        @RequestParam(value = "propertySearch", required = false) String propertySearch,
                                                        @RequestParam(value = "refundIds", required = false) List<Integer> refundIds
    ) {

        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var customPaging = bookingADService.getBookingList(email,
                pageNumber,
                pageSize,
                hostSearch,
                customerSearch,
                bookingType,
                startDate,
                endDate,
                locationIds,
                status,
                propertySearch,
                refundIds
        );

        return ResponseEntity.ok(customPaging);

    }
}
