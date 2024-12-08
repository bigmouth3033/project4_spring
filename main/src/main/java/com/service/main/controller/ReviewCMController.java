package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.dto.RateByHostDto;
import com.service.main.service.customer.ReviewCMService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reviewCM")
public class ReviewCMController {

    @Autowired
    private ReviewCMService reviewCMService;

    @PostMapping("review_by_host")
    @RolesAllowed("USER")
    public ResponseEntity<CustomResult> reviewByHost(@ModelAttribute RateByHostDto rateByHostDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        var customResult = reviewCMService.rateByHost(email, rateByHostDto);
        return ResponseEntity.ok(customResult);
    }

}
