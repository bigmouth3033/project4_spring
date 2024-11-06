package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.service.customer.ManagedCityCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("managedCityCM")
public class ManagedCityCMController {

    @Autowired
    private ManagedCityCMService managedCityCMService;


    @GetMapping
    public ResponseEntity<CustomResult> getManagedCities(){
        var customResult = managedCityCMService.getManagedCity();

        return ResponseEntity.ok(customResult);
    }
}
