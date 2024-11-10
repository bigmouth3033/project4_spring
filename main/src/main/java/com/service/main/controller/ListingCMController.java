package com.service.main.controller;

import com.service.main.dto.CustomPaging;
import com.service.main.dto.CustomResult;
import com.service.main.dto.PropertyDto;
import com.service.main.service.customer.ListingCMService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("listingCM")
public class ListingCMController {

    @Autowired
    private ListingCMService listingCMService;

    @PostMapping("initial")
    public ResponseEntity<CustomResult> initializeListing(){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var customResult = listingCMService.initializeListing(email);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("get_host_listing_by_id")
    public ResponseEntity<CustomResult> getListingByID(@RequestParam Integer id){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();

        var customResult = listingCMService.getListing(email, id);

        return ResponseEntity.ok(customResult);
    }

    @PostMapping("update_listing")
    public ResponseEntity<CustomResult> updateListing(@ModelAttribute PropertyDto property){
        var customResult = listingCMService.updateListing(property);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("get_host_listings")
    public ResponseEntity<CustomPaging> getListings(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam(required = false, defaultValue = "") String search, @RequestParam String status){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var customPaging = listingCMService.getHostListing(pageNumber, pageSize, email, search, status);
        return ResponseEntity.ok(customPaging);
    }

    @GetMapping("get_host_calendar_list")
    public ResponseEntity<CustomResult> getHostCalendarList(){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();

        var customResult = listingCMService.getAllListingOfHost(email);
        return ResponseEntity.ok(customResult);
    }

}
