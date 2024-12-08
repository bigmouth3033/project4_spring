package com.service.main.controller;

import com.service.main.dto.CustomPaging;
import com.service.main.dto.CustomResult;
import com.service.main.dto.PropertyDto;
import com.service.main.dto.UpdateExceptionDateDto;
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

    @PutMapping("update_exception_date")
    public ResponseEntity<CustomResult> updateExceptionDate(@ModelAttribute UpdateExceptionDateDto updateExceptionDateDto){
        var customResult = listingCMService.changePriceForDates(updateExceptionDateDto.getPropertyId(), updateExceptionDateDto.getStart(), updateExceptionDateDto.getEnd(), updateExceptionDateDto.getPrice());
        return ResponseEntity.ok(customResult);
    }

    @PutMapping("update_not_available_date")
    public ResponseEntity<CustomResult> updateNotAvailableDate(@ModelAttribute UpdateExceptionDateDto updateExceptionDateDto){
        var customResult = listingCMService.blockDate(updateExceptionDateDto.getPropertyId(), updateExceptionDateDto.getStart(), updateExceptionDateDto.getEnd());
        return ResponseEntity.ok(customResult);
    }

    @PutMapping("open_not_available_date")
    public ResponseEntity<CustomResult> openNotAvailableDate(@ModelAttribute UpdateExceptionDateDto updateExceptionDateDto){
        var customResult = listingCMService.openDate(updateExceptionDateDto.getPropertyId(), updateExceptionDateDto.getStart(), updateExceptionDateDto.getEnd());
        return ResponseEntity.ok(customResult);
    }

    @PutMapping("public_request")
    public ResponseEntity<CustomResult> publicRequest(int propertyID){
        var customResult = listingCMService.pendingRequest(propertyID);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping
    public ResponseEntity<CustomResult> getProperty(@RequestParam int id) {
        CustomResult result = listingCMService.getListingById(id);
        return ResponseEntity.ok(result);
    }


}
