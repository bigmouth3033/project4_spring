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
    @RolesAllowed({"ADMIN", "CATEGORY_MANAGEMENT"})
    public ResponseEntity<CustomPaging> getCategoryPaging(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam(required = false, defaultValue = "") String search, @RequestParam String status){
        var customPaging = categoryADService.getCategoryPaging(pageNumber,pageSize,search,status);
        return ResponseEntity.ok(customPaging);
    }

    @GetMapping("find_by_id")
    @RolesAllowed({"ADMIN", "CATEGORY_MANAGEMENT"})
    public ResponseEntity<CustomResult> findCategoryById(@RequestParam(required = true) int id){
        var customResult = categoryADService.getCategoryById(id);
        return ResponseEntity.ok(customResult);
    }

    @PutMapping
    @RolesAllowed({"ADMIN", "CATEGORY_MANAGEMENT"})
    public ResponseEntity<CustomResult> updateCategory(@ModelAttribute UpdateCategoryDto updateCategoryDto){
        var customResult = categoryADService.updateCategory(updateCategoryDto);
        return ResponseEntity.ok(customResult);
    }

    @PostMapping
    @RolesAllowed({"ADMIN", "CATEGORY_MANAGEMENT"})
    public ResponseEntity<CustomResult> createCategory(@ModelAttribute CreateCategoryDto createCategoryDto){
        var customResult = categoryADService.createNewCategory(createCategoryDto);
        return ResponseEntity.ok(customResult);
    }

    @PutMapping("change_category_status")
    @RolesAllowed({"ADMIN", "CATEGORY_MANAGEMENT"})
    public ResponseEntity<CustomResult> updateCategoryStatus(@ModelAttribute ChangeCategoryStatusDto changeCategoryStatusDto){
        var customResult = categoryADService.changeCategoryStatus(changeCategoryStatusDto);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("get_all_categories")
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public ResponseEntity<CustomResult> getCategories(){
        var customResult = categoryADService.getAllCategory();
        return ResponseEntity.ok(customResult);
    }
}
