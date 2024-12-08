package com.service.main.service.admin;

import com.service.main.dto.*;
import com.service.main.entity.Amenity;
import com.service.main.repository.AmenityRepository;
import com.service.main.service.ImageUploadingService;
import com.service.main.service.PagingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AmenityADService {

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private ImageUploadingService imageUploadingService;

    @Autowired
    private PagingService pagingService;

    public CustomResult getAmenityType(){
        try{
            var types = amenityRepository.getTypeOfAmenity();
            return new CustomResult(200, "Success", types);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", null);
        }
    }

    public CustomResult findById(int id){
        try{
            var amenity = amenityRepository.findById(id);

            if(amenity.isEmpty()){
                return new CustomResult(404, "Not Found", null);
            }

            return new CustomResult(200, "Success", amenity.get());
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", null);
        }
    }

    public CustomResult updateAmenity(UpdateAmenityDto updateAmenityDto){
        try{
            var amenity = amenityRepository.findById(updateAmenityDto.getId());

            if(amenity.isEmpty()){
                return new CustomResult(404, "Not Found", null);
            }

            amenity.get().setName(updateAmenityDto.getName());
            amenity.get().setDescription(updateAmenityDto.getDescription());
            amenity.get().setType(updateAmenityDto.getType());

            if(updateAmenityDto.getImage() != null){
                amenity.get().setImage(imageUploadingService.upload(updateAmenityDto.getImage()));
            }

            amenityRepository.save(amenity.get());

            return new CustomResult(200, "Success", null);

        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    public CustomPaging getAmenity(int pageNumber, int pageSize, String search, String status){
        try{
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
            if(status.equals("true")){
                var pagedAmenity = amenityRepository.getAmenity(search, true, pageable);
                return pagingService.convertToCustomPaging(pagedAmenity, pageNumber, pageSize);
            }

            if(status.equals("false")){
                var pagedAmenity = amenityRepository.getAmenity(search, false, pageable);
                return pagingService.convertToCustomPaging(pagedAmenity, pageNumber, pageSize);
            }

            var pagedAmenity = amenityRepository.getAmenity(search, null, pageable);
            return pagingService.convertToCustomPaging(pagedAmenity, pageNumber, pageSize);

        }catch (Exception ex){
            return new CustomPaging();
        }
    }

    public CustomResult createNewAmenity(CreateAmenityDto amenityDto){
        try{
            var newAmenity = new Amenity();
            newAmenity.setStatus(true);
            newAmenity.setName(amenityDto.getName());
            newAmenity.setDescription(amenityDto.getDescription());
            newAmenity.setType(amenityDto.getType());
            newAmenity.setImage(imageUploadingService.upload(amenityDto.getImage()));

            amenityRepository.save(newAmenity);

            return new CustomResult(200, "Success", null);

        }catch (Exception ex){
            return new CustomResult(400, "Bad request", null);
        }
    }

    public CustomResult changeAmenityStatus(ChangeAmenityStatusDto changeAmenityStatusDto){
        try{
            var amenity = amenityRepository.findById(changeAmenityStatusDto.getId());

            if(amenity.isEmpty()){
                return new CustomResult(404, "Not Found", null);
            }
            amenity.get().setStatus(changeAmenityStatusDto.isStatus());
            amenityRepository.save(amenity.get());
            return new CustomResult(200, "Success", null);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", null);
        }
    }


    public CustomResult getAllAmenity(){
        try{
            var amenities = amenityRepository.findAll();

            var list = amenities.stream().map(amenity -> {
                var amenityDto = new AmenityDto();
                BeanUtils.copyProperties(amenity, amenityDto);

                return amenityDto;
            }).toList();

            return new CustomResult(200, "Success", list);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", null);
        }
    }

}
