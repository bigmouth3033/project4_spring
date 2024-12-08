package com.service.main.service.customer;

import com.service.main.dto.CustomResult;
import com.service.main.repository.ManagedCityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagedCityCMService {

    @Autowired
    private ManagedCityRepository managedCityRepository;


    public CustomResult getManagedCity(){
        try{
            var managedCities = managedCityRepository.findManagedCity();

            return new CustomResult(200, "Success", managedCities);

        }catch (Exception e){
            return new CustomResult(400, "Bad request", null);
        }
    }



}
