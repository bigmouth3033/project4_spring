package com.service.main.controller;

import com.service.main.dto.*;
import com.service.main.service.admin.CategoryADService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("categoryAD")
public class CategoryADController {

    @Autowired
    private CategoryADService categoryADService;

    @GetMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CustomPaging> getCategoryPaging(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam(required = false, defaultValue = "") String search, @RequestParam String status){
        var customPaging = categoryADService.getCategoryPaging(pageNumber,pageSize,search,status);
        return ResponseEntity.ok(customPaging);
    }

    @GetMapping("find_by_id")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CustomResult> findAmenityById(@RequestParam(required = true) int id){
        var customResult = categoryADService.getCategoryById(id);
        return ResponseEntity.ok(customResult);
    }

    @PutMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CustomResult> updateAmenity(@ModelAttribute UpdateCategoryDto updateCategoryDto){
        var customResult = categoryADService.updateCategory(updateCategoryDto);
        return ResponseEntity.ok(customResult);
    }

    @PostMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<CustomResult> createNewAmenity(@ModelAttribute CreateCategoryDto createCategoryDto){
        var customResult = categoryADService.createNewCategory(createCategoryDto);
        return ResponseEntity.ok(customResult);
    }
}
