package com.service.main.controller;

import com.service.main.service.ImageUploadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private ImageUploadingService imageUploadingService;

    @PostMapping
    public ResponseEntity<String> uploadImage(MultipartFile file) {
        var image = imageUploadingService.upload(file);
        return ResponseEntity.ok(image);
    }
}
