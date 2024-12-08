package com.service.main.service.admin;

import com.service.main.dto.*;
import com.service.main.repository.AdminRepository;
import com.service.main.repository.PropertyRepository;
import com.service.main.repository.RoleRepository;
import com.service.main.service.PagingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListingADService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PagingService pagingService;

    public CustomResult getListingById(int listingId){
        try{
            var property = propertyRepository.findById(listingId);

            if(property.isEmpty()){
                return new CustomResult(404, "Not found", null);
            }

            var newPropertyDto = new PropertyAdminDto();
            BeanUtils.copyProperties(property.get(), newPropertyDto);

            var categoryDto = new CategoryDto();
            BeanUtils.copyProperties(property.get().getPropertyCategory(), categoryDto);

            var userDto = new UserDto();
            BeanUtils.copyProperties(property.get().getUser(), userDto);


            List<AmenityDto> amenities = new ArrayList<>();

            for(var amenity : property.get().getPropertyAmenities()){
                var amenityDto = new AmenityDto();
                amenityDto.setName(amenity.getAmenity().getName());
                amenityDto.setImage(amenity.getAmenity().getImage());
                amenities.add(amenityDto);
            }

            var refundDto = new RefundDto();
            refundDto.setPolicyName(property.get().getRefundPolicy().getPolicyName());
            refundDto.setPolicyDescription(property.get().getRefundPolicy().getPolicyDescription());

            List<String> imageList = new ArrayList<>();

            for(var image : property.get().getPropertyImages()){
                imageList.add(image.getImageName());
            }

            newPropertyDto.setPropertyImages(imageList);
            newPropertyDto.setPropertyCategory(categoryDto);
            newPropertyDto.setUser(userDto);
            newPropertyDto.setPropertyAmenities(amenities);
            newPropertyDto.setRefund(refundDto);

            return new CustomResult(200, "OK", newPropertyDto);

        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }


    public CustomResult changeListingStatus(ChangeListingStatusDto changeListingStatusDto) {
        try{
            var listing = propertyRepository.findListingById(changeListingStatusDto.getId());

            if(listing == null){
                return new CustomResult(404, "Not found", null);
            }

            listing.setStatus(changeListingStatusDto.getStatus());
            listing.setSuggestion(changeListingStatusDto.getSuggestion());

            propertyRepository.save(listing);
            return new CustomResult(200, "OK", null);

        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    public CustomPaging getListingList(String employeeEmail,
                                       int pageNumber,
                                       int pageSize, String status,
                                       String propertySearchText,
                                       String searchHost, String bookType,
                                       List<Integer> locationsIds,
                                       List<Integer> amenityIds,
                                       List<Integer> categoryIds
    ){
        try{
            var employee = adminRepository.findByEmail(employeeEmail);

            if(employee == null){
                var customPaging = new CustomPaging();
                customPaging.setMessage("Employee not found");
                customPaging.setStatus(403);
                return customPaging;
            }

            boolean isAdmin = false;

            for(var empRole : employee.getAdminRoles()){
                var role = roleRepository.findById(empRole.getRole().getId());

                if(role.get().getRoleName().equals("ADMIN")){
                    isAdmin = true;
                    break;
                }
            }

            List<Integer> employeeManagedCity = employee.getAdminManageCities().stream().map(city -> city.getManagedCity().getId()).toList();

            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updatedAt").descending());

            var getListing = propertyRepository.searchListingByAdmin(isAdmin,
                    employeeManagedCity,
                    status,
                    propertySearchText,
                    searchHost,
                    bookType,
                    locationsIds,
                    amenityIds,
                    categoryIds,
                    pageable);

            List<PropertyAdminDto> list = getListing.getContent().stream().map(listing -> {
                var newPropertyDto = new PropertyAdminDto();
                BeanUtils.copyProperties(listing, newPropertyDto);

                var categoryDto = new CategoryDto();
                BeanUtils.copyProperties(listing.getPropertyCategory(), categoryDto);

                var userDto = new UserDto();
                BeanUtils.copyProperties(listing.getUser(), userDto);


                List<AmenityDto> amenities = new ArrayList<>();

                for(var amenity : listing.getPropertyAmenities()){
                    var amenityDto = new AmenityDto();
                    amenityDto.setName(amenity.getAmenity().getName());
                    amenityDto.setImage(amenity.getAmenity().getImage());
                    amenities.add(amenityDto);
                }

                var refundDto = new RefundDto();
                refundDto.setPolicyName(listing.getRefundPolicy().getPolicyName());

                List<String> imageList = new ArrayList<>();

                for(var image : listing.getPropertyImages()){
                    imageList.add(image.getImageName());
                }

                newPropertyDto.setPropertyImages(imageList);
                newPropertyDto.setPropertyCategory(categoryDto);
                newPropertyDto.setUser(userDto);
                newPropertyDto.setPropertyAmenities(amenities);
                newPropertyDto.setRefund(refundDto);
                return newPropertyDto;
            }).toList();

            Page<PropertyAdminDto> updatedPage = new PageImpl<>(list, pageable, getListing.getTotalElements());

            return pagingService.convertToCustomPaging(updatedPage, pageNumber, pageSize);

        }catch (Exception e){
            var customPaging = new CustomPaging();
            customPaging.setMessage( e.getMessage());
            customPaging.setStatus(400);
            return customPaging;
        }
    }
}
