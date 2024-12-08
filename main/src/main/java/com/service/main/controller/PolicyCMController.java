package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.service.customer.PolicyCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("policyCM")
public class PolicyCMController {

    @Autowired
    private PolicyCMService policyCMService;

    @GetMapping
    public ResponseEntity<CustomResult> getPolicies(){
        var customResult = policyCMService.getServices();
        return ResponseEntity.ok(customResult);
    }

}
