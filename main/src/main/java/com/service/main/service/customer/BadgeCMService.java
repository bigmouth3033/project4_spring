package com.service.main.service.customer;

import com.service.main.dto.CustomResult;
import com.service.main.repository.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BadgeCMService {

    @Autowired
    private BadgeRepository badgeRepository;


    public CustomResult getUserBadge(){
        try{
            var badges = badgeRepository.findUserBadge();
            return new CustomResult(200, "Success", badges);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    public CustomResult getHostBadge(){
        try{
            var badges = badgeRepository.findHostBadge();
            return new CustomResult(200, "Success", badges);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

}
