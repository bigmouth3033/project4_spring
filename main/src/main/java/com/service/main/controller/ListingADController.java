package com.service.main.controller;

import com.service.main.dto.ChangeListingStatusDto;
import com.service.main.dto.CustomPaging;
import com.service.main.dto.CustomResult;
import com.service.main.service.admin.ListingADService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("listingAD")
public class ListingADController {

    @Autowired
    private ListingADService listingADService;

    @PostMapping("change_status")
    @RolesAllowed({"ADMIN", "PROPERTY_MANAGEMENT"})
    public ResponseEntity<CustomResult> changeListingStatus(@ModelAttribute ChangeListingStatusDto changeListingStatusDto) {
        var customResult = listingADService.changeListingStatus(changeListingStatusDto);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("read_property_by_id")
    @RolesAllowed({"ADMIN", "PROPERTY_MANAGEMENT"})
    public ResponseEntity<CustomResult> getListingById(@RequestParam Integer id) {
        var customResult = listingADService.getListingById(id);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("get_listing")
    @RolesAllowed({"ADMIN", "PROPERTY_MANAGEMENT"})
    public ResponseEntity<CustomPaging> getListing(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam String status,
            @RequestParam(required = false, defaultValue = "") String propertySearchText,
            @RequestParam(required = false, defaultValue = "") String searchHost,
            @RequestParam String bookType,
            @RequestParam(required = false) List<Integer> locationsIds,
            @RequestParam(required = false) List<Integer> amenityIds,
             @RequestParam(required = false) List<Integer> categoryIds
    ){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var customPaging = listingADService.getListingList(
                email,
                pageNumber,
                pageSize,
                status,
                propertySearchText,
                searchHost,
                bookType,
                locationsIds,
                amenityIds,
                categoryIds
        );
        return ResponseEntity.ok(customPaging);
    }
}
