package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.service.customer.CategoryCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categoryCM")
public class CategoryCMController {

    @Autowired
    private CategoryCMService categoryCMService;

    @GetMapping
    public ResponseEntity<CustomResult> getCategories(){
        var customResult = categoryCMService.getCategories();
        return ResponseEntity.ok(customResult);
    }
}
