package com.service.main.service.customer;

import com.service.main.dto.CustomResult;
import com.service.main.repository.PropertyCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryCMService {

    @Autowired
    private PropertyCategoryRepository propertyCategoryRepository;

    public CustomResult getCategories(){
        try{
            var categories = propertyCategoryRepository.getAvailableCategory();

            return new CustomResult(200, "Success", categories);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }
}
