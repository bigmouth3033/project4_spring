package com.service.main.service.customer;

import com.service.main.dto.CustomResult;
import com.service.main.repository.AmenityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmenityCMService {


    @Autowired
    private AmenityRepository amenityRepository;


    public CustomResult getAmenities(){
        try{
            var amenities = amenityRepository.findAll();

            return new CustomResult(200, "Success", amenities);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", null);

        }
    }
}
