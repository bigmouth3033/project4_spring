package com.service.main.service.admin;

import com.service.main.dto.*;
import com.service.main.entity.PropertyCategory;
import com.service.main.repository.PropertyCategoryRepository;
import com.service.main.service.ImageUploadingService;
import com.service.main.service.PagingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

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
            Page<PropertyCategory> pagedCategory = null;

            if(status.equals("true")){
                 pagedCategory = propertyCategoryRepository.getCategory(search, true, pageable);

            }

            if(status.equals("false")){
                 pagedCategory = propertyCategoryRepository.getCategory(search, false, pageable);

            }

            if(!status.equals("true") && !status.equals("false")){
                pagedCategory = propertyCategoryRepository.getCategory(search, null, pageable);
            }

             List<CategoryDto> categoryDtoList = pagedCategory.getContent().stream().map(category -> {
                 CategoryDto categoryDto = new CategoryDto();
                 BeanUtils.copyProperties(category, categoryDto);
                 categoryDto.setPropertyCount(category.getProperties().size());
                 return categoryDto;
             }).toList();

            Page<CategoryDto> updatedPage = new PageImpl<>(categoryDtoList, pageable, pagedCategory.getTotalElements());

            return pagingService.convertToCustomPaging(updatedPage, pageNumber, pageSize);
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

    public CustomResult changeCategoryStatus(@ModelAttribute ChangeCategoryStatusDto changeCategoryStatusDto){
        try{
            var category = propertyCategoryRepository.findById(changeCategoryStatusDto.getId());

            if(category.isEmpty()){
                return new CustomResult(404, "Not found", null);
            }

            category.get().setStatus(changeCategoryStatusDto.isStatus());

            propertyCategoryRepository.save(category.get());
            return new CustomResult(200, "Success", null);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    public CustomResult getAllCategory(){
        try{
            var categories = propertyCategoryRepository.findAll();

            List<CategoryDto> categoryDtoList = categories.stream().map(category -> {
                var categoryDto = new CategoryDto();
                BeanUtils.copyProperties(category, categoryDto);
                return categoryDto;
            }).toList();

            return new CustomResult(200, "OK", categoryDtoList);
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
