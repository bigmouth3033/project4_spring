package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.service.customer.AmenityCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("amenityCM")
public class AmenityCMController {

    @Autowired
    private AmenityCMService amenityCMService;

    @GetMapping
    public ResponseEntity<CustomResult> getAmenities(){
        var customResult = amenityCMService.getAmenities();
        return ResponseEntity.ok(customResult);
    }
}
