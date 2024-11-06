package com.service.main.service.admin;

import com.service.main.dto.CreateCategoryDto;
import com.service.main.dto.CustomPaging;
import com.service.main.dto.CustomResult;
import com.service.main.dto.UpdateCategoryDto;
import com.service.main.entity.PropertyCategory;
import com.service.main.repository.PropertyCategoryRepository;
import com.service.main.service.ImageUploadingService;
import com.service.main.service.PagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CategoryADService {

    @Autowired
    private PropertyCategoryRepository propertyCategoryRepository;

    @Autowired
    private ImageUploadingService imageUploadingService;

    @Autowired
    private PagingService pagingService;


    public CustomPaging getCategoryPaging(int pageNumber, int pageSize, String search, String status){
        try{
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
            if(status.equals("true")){
                var pagedCategory = propertyCategoryRepository.getCategory(search, true, pageable);
                return pagingService.convertToCustomPaging(pagedCategory, pageNumber, pageSize);
            }

            if(status.equals("false")){
                var pagedCategory = propertyCategoryRepository.getCategory(search, false, pageable);
                return pagingService.convertToCustomPaging(pagedCategory, pageNumber, pageSize);
            }

            var pagedCategory = propertyCategoryRepository.getCategory(search, null, pageable);
            return pagingService.convertToCustomPaging(pagedCategory, pageNumber, pageSize);
        }catch (Exception e){
            return new CustomPaging();
        }
    }

    public CustomResult getCategoryById(int id){
        try{
            var category = propertyCategoryRepository.findById(id);

            if(category.isEmpty()){
                return new CustomResult(404, "Not found", null);
            }

            return new CustomResult(200, "OK", category.get());
        }catch (Exception e){
            return new CustomResult(400, "Bad request", e.getMessage());
        }
    }


    public CustomResult createNewCategory(CreateCategoryDto createCategoryDto){
        try{
            var newCategory = new PropertyCategory();
            newCategory.setCategoryName(createCategoryDto.getCategoryName());
            newCategory.setDescription(createCategoryDto.getDescription());
            newCategory.setCategoryImage(imageUploadingService.upload(createCategoryDto.getCategoryImage()));
            newCategory.setStatus(true);

            propertyCategoryRepository.save(newCategory);

            return new CustomResult(200, "Success", null);

        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    public CustomResult updateCategory(UpdateCategoryDto updateCategoryDto){
        try{
            var category = propertyCategoryRepository.findById(updateCategoryDto.getId());

            if(category.isEmpty()){
                return new CustomResult(404, "Not found", null);
            }


            category.get().setCategoryName(updateCategoryDto.getCategoryName());
            category.get().setDescription(updateCategoryDto.getDescription());

            if(updateCategoryDto.getCategoryImage() != null){
                category.get().setCategoryImage(imageUploadingService.upload(updateCategoryDto.getCategoryImage()));
            }

            propertyCategoryRepository.save(category.get());

            return new CustomResult(200, "Success", null);

        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }
}
