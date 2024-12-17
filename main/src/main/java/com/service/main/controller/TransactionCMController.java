package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.dto.TransactionDto;
import com.service.main.service.customer.TransactionCmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transaction")
public class TransactionCMController {
    @Autowired
    private TransactionCmService transactionCmService;

    @PostMapping("booking_escrow")
    public ResponseEntity<CustomResult> createTransaction(@ModelAttribute TransactionDto transactionDto) {

        CustomResult result = transactionCmService.createTransaction(transactionDto);
        return ResponseEntity.ok(result);
    }
}