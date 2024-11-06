package com.service.main.controller;

import com.service.main.dto.ChangeCityStatusDto;
import com.service.main.dto.CustomPaging;
import com.service.main.dto.CustomResult;
import com.service.main.service.admin.CityADService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cityAD")
public class CityADController {
    @Autowired
    private CityADService cityADService;

    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    @GetMapping
    public ResponseEntity<CustomPaging> getCities(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam(required = false, defaultValue = "") String cityName, @RequestParam String status){
        var customPaging = cityADService.getCityList(pageNumber, pageSize, cityName, status);
        return ResponseEntity.ok(customPaging);
    }

    @PostMapping
    @RolesAllowed({"ADMIN","UPDATE_CITY"})
    public ResponseEntity<CustomResult> getCities(@ModelAttribute ChangeCityStatusDto changeCityStatusDto){
        var customResult =  cityADService.changeCityStatus(changeCityStatusDto);
        return ResponseEntity.ok(customResult);
    }
}
