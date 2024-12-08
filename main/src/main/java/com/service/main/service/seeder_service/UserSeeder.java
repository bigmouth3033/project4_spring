package com.service.main.service.seeder_service;

import com.service.main.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSeeder {

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserSeeder.class);

    @Transactional
    public void seedUsers(int numberOfUsers) {
        try{
            for(int i = 0; i < numberOfUsers; i++){
                log.info("Seeding user {}", i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
