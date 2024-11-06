package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.service.customer.BadgeCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("badgeCM")
public class BadgeCMController {

    @Autowired
    private BadgeCMService badgeCMService;

    @GetMapping("user_badge")
    public ResponseEntity<CustomResult> getUserBadge(){
        var customResult = badgeCMService.getUserBadge();
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("host_badge")
    public ResponseEntity<CustomResult> getHostBadge(){
        var customResult = badgeCMService.getHostBadge();
        return ResponseEntity.ok(customResult);
    }


}
