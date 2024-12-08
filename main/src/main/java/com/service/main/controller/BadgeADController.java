package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.service.admin.BadgeADService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("badgeAD")
public class BadgeADController {

    @Autowired
    private BadgeADService badgeADService;

    @GetMapping
    public ResponseEntity<CustomResult> getAllBadges(){
        var customresult = badgeADService.getAllBadges();
        return ResponseEntity.ok(customresult);
    }
}
