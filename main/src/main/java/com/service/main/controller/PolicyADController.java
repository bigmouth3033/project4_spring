package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.service.admin.RefundADService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("PolicyAD")
public class PolicyADController {

    @Autowired
    private RefundADService refundADService;

    @GetMapping("get_all")
    public ResponseEntity<CustomResult> getAll() {
        var customResult = refundADService.getAllRefundPolicy();

        return ResponseEntity.ok(customResult);
    }
}
