package com.service.main.controller;

import com.service.main.repository.BookingRepository;
import com.service.main.service.ImageUploadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private ImageUploadingService imageUploadingService;

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping
    public ResponseEntity<List<String>> uploadImage(List<MultipartFile> files) {

        List<String> images = files.stream().map((file) -> {
            return imageUploadingService.upload(file);
        }).toList();

        List<Integer> t = new ArrayList<>() ;

        return ResponseEntity.ok(images);
    }

    @GetMapping("test_expire_reserved")
    public ResponseEntity<?> expireReserved() {
        return ResponseEntity.ok(bookingRepository.getExpiredReservationBooking(new Date()));
    }

    @GetMapping("test_payment_ready")
    public ResponseEntity<?> paymentReady() {
        return ResponseEntity.ok(bookingRepository.getReadyToFinishPayment(new Date()));
    }
}
