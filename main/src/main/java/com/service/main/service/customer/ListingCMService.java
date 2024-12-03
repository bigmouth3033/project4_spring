package com.service.main.service.customer;

import com.service.main.dto.CustomResult;
import com.service.main.dto.PropertyDto;
import com.service.main.entity.Property;
import com.service.main.entity.PropertyAmenity;
import com.service.main.entity.PropertyAmenityId;
import com.service.main.entity.PropertyImage;
import com.service.main.repository.*;
import com.service.main.service.ImageUploadingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ListingCMService {


    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagedCityRepository managedCityRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private PropertyAmenityRepository propertyAmenityRepository;

    @Autowired
    private ImageUploadingService imageUploadingService;

    @Autowired
    private RefundPolicyRepository refundPolicyRepository;

    @Autowired
    private PropertyCategoryRepository propertyCategoryRepository;

    @Autowired
    private PropertyImageRepository propertyImageRepository;

    @Autowired
    private BadgeRepository badgeRepository;


    public CustomResult initializeListing(String email){
        try{

            var user = userRepository.findUserByEmail(email);
            if(user == null){
                return new CustomResult(404, "User not found", null);
            }

            var newProperty = new Property();
            newProperty.setStatus("PROGRESS");
            newProperty.setUser(user);
            propertyRepository.save(newProperty);
            return new CustomResult(200, "Success", newProperty.getId());
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    public CustomResult getListing(String email, int id){
        try{
            var user = userRepository.findUserByEmail(email);
            if(user == null){
                return new CustomResult(404, "User not found", null);
            }

            var listing = propertyRepository.findByIdAndUserId(id, user.getId());

            if(listing == null){
                return new CustomResult(404, "Listing not found", null);
            }

            var propertyDto = new PropertyDto();

            BeanUtils.copyProperties(listing, propertyDto);
            if(listing.getManagedCity() != null){
                propertyDto.setManagedCityId(listing.getManagedCity().getId());
            }

            if(listing.getRefundPolicy() != null){
                propertyDto.setRefundPolicyId(listing.getRefundPolicy().getId());
            }

            propertyDto.setUserId(listing.getUser().getId());

            if(listing.getPropertyCategory() != null){
                propertyDto.setPropertyCategoryID(listing.getPropertyCategory().getId());
            }

            if(listing.getInstantBookRequirement() != null){
                propertyDto.setInstantBookRequirementID(listing.getInstantBookRequirement().getId());
            }

            List<Integer> amenityList = new ArrayList<>();

            for(var amenity : listing.getPropertyAmenities()){
                amenityList.add(amenity.getAmenity().getId());
            }
            propertyDto.setPropertyAmenities(amenityList);

            List<String> imageList = new ArrayList<>();

            for(var image : listing.getPropertyImages()){
                imageList.add(image.getImageName());
            }

            propertyDto.setPropertyImages(imageList);

            return new CustomResult(200, "Success", propertyDto);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    @Transactional
    public CustomResult updateListing(PropertyDto propertyDto){
        try{
            var property = propertyRepository.findListingById(propertyDto.getId());

            BeanUtils.copyProperties(propertyDto, property);


            if(propertyDto.getManagedCityId() != null){
                var managedCity = managedCityRepository.findById(propertyDto.getManagedCityId());
                property.setManagedCity(managedCity.get());
            }

            if(propertyDto.getRefundPolicyId() != null){
                var refundPolicy = refundPolicyRepository.findById(propertyDto.getRefundPolicyId());
                property.setRefundPolicy(refundPolicy.get());
            }

            if(propertyDto.getPropertyCategoryID() != null){
                var propertyCategory = propertyCategoryRepository.findById(propertyDto.getPropertyCategoryID());
                property.setPropertyCategory(propertyCategory.get());
            }

            if(propertyDto.getInstantBookRequirementID() != null){
                var badge = badgeRepository.findById(propertyDto.getInstantBookRequirementID());
                property.setInstantBookRequirement(badge.get());
            }

            propertyImageRepository.deleteAllByPropertyId(property.getId());

            if(propertyDto.getPropertyImages() != null){
                for(var image: propertyDto.getPropertyImages()){
                    var newImage = new PropertyImage();
                    newImage.setImageName(image);
                    newImage.setProperty(property);
                    propertyImageRepository.save(newImage);

                }
            }

            if(propertyDto.getNewImages() != null){
                for(var obj: propertyDto.getNewImages()){
                    var image = imageUploadingService.upload(obj);
                    var newImage = new PropertyImage();
                    newImage.setImageName(image);
                    newImage.setProperty(property);
                    propertyImageRepository.save(newImage);
                }
            }

            // delete all amenity and then update new one
            propertyAmenityRepository.deleteAllByPropertyId(property.getId());


            if(propertyDto.getPropertyAmenities() != null){
                for(var amenityId: propertyDto.getPropertyAmenities()){
                    var amenity = amenityRepository.findById(amenityId);
                    var propertyAmenityId = new PropertyAmenityId(property.getId(), amenityId);
                    var newAmenity = new PropertyAmenity();
                    newAmenity.setId(propertyAmenityId);
                    newAmenity.setProperty(property);
                    newAmenity.setAmenity(amenity.get());
                    propertyAmenityRepository.save(newAmenity);
                }
            }



            property.setUpdatedAt(new Date());
            propertyRepository.save(property);

            return new CustomResult(200, "Success", null);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    public CustomResult getAllProperties(){
        try{
            var properties = propertyRepository.findAll();
            return new CustomResult(200, "Success", properties);
        } catch (Exception e) {
            return new CustomResult(400, "Bad Request", e.getMessage());
        }

    }

}
