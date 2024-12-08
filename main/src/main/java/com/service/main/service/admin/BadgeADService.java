package com.service.main.service.admin;

import com.service.main.dto.CustomResult;
import com.service.main.repository.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BadgeADService {

    @Autowired
    private BadgeRepository badgeRepository;

    public CustomResult getAllBadges(){
        try{
            var badges = badgeRepository.findAll();

            return new CustomResult(200, "Success", badges);

        }catch (Exception ex){
            return new CustomResult(400, ex.getMessage(), null);
        }
    }
}
