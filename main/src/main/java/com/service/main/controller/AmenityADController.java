package com.service.main.controller;

import com.service.main.dto.CreateAmenityDto;
import com.service.main.dto.CustomPaging;
import com.service.main.dto.CustomResult;
import com.service.main.dto.UpdateAmenityDto;
import com.service.main.service.admin.AmenityADService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("amenityAD")
public class AmenityADController {

    @Autowired
    private AmenityADService amenityADService;

    @GetMapping("type")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CustomResult> getAmenityType(){
        var customResult = amenityADService.getAmenityType();
        return ResponseEntity.ok(customResult);
    }

    @GetMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CustomPaging> getAmenityPaging(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam(required = false, defaultValue = "") String search, @RequestParam String status){
        var customPaging = amenityADService.getAmenity(pageNumber,pageSize,search,status);
        return ResponseEntity.ok(customPaging);
    }

    @GetMapping("find_by_id")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CustomResult> findAmenityById(@RequestParam(required = true) int id){
        var customResult = amenityADService.findById(id);
        return ResponseEntity.ok(customResult);
    }

    @PutMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CustomResult> updateAmenity(@ModelAttribute UpdateAmenityDto updateAmenityDto){
        var customResult = amenityADService.updateAmenity(updateAmenityDto);
        return ResponseEntity.ok(customResult);
    }

    @PostMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CustomResult> createNewAmenity(@ModelAttribute CreateAmenityDto createAmenityDto){
        var customResult = amenityADService.createNewAmenity(createAmenityDto);
        return ResponseEntity.ok(customResult);
    }


}
