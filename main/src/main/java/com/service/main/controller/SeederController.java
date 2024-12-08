package com.service.main.controller;

import com.service.main.service.seeder_service.UserSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("seeder")
public class SeederController {

    @Autowired
    private UserSeeder userSeeder;

    @GetMapping("seed_user")
    public void seedUser(){
        userSeeder.seedUsers(10);
    }
}
