package com.service.main.service.customer;

import com.service.main.dto.CustomPaging;
import com.service.main.dto.CustomResult;
import com.service.main.dto.PropertyDto;
import com.service.main.dto.PropertyGiuDto;
import com.service.main.entity.*;
import com.service.main.repository.*;
import com.service.main.service.ImageUploadingService;
import com.service.main.service.PagingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private PagingService pagingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PropertyExceptionDateRepository propertyExceptionDateRepository;

    @Autowired
    private PropertyNotAvailableDateRepository propertyNotAvailableDateRepository;


    public CustomResult pendingRequest(int propertyId){
        try{
            var property = propertyRepository.findById(propertyId);

            if(property.isEmpty()){
                return new CustomResult(404, "Not found", null);
            }

            property.get().setStatus("PENDING");
            property.get().setSuggestion("");

            propertyRepository.save(property.get());
            return new CustomResult(200, "Success", null);

        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }


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
            }else{
                property.setInstantBookRequirement(null);
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

    public CustomPaging getHostListing(int pageNumber, int pageSize, String email, String search, String status){
        try{
            var user = userRepository.findUserByEmail(email);

            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updatedAt").descending());

            var listings = propertyRepository.findListingByUserId(user.getId(), search, status, pageable);

            return pagingService.convertToCustomPaging(listings, pageNumber, pageSize);
        } catch (Exception e) {
            return new CustomPaging();
        }
    }

    public CustomResult openDate(int propertyId, String start, String end){
        try{
            var property = propertyRepository.findListingById(propertyId);

            if(property == null){
                return new CustomResult(404, "Property not found", null);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(start);
            Date endDate = dateFormat.parse(end);

            var checkList = bookingRepository.checkIfBlockable(propertyId, startDate, endDate);

            if(!checkList.isEmpty()){
                return new CustomResult(403, "Some one already book that day", checkList);
            }

            Calendar calendar = Calendar.getInstance();

            calendar.setTime(startDate);

            while (!calendar.getTime().after(endDate)) {
                var checkExist = propertyNotAvailableDateRepository.findByPropertyIdAndDate(propertyId, calendar.getTime());

                if(checkExist != null){
                    propertyNotAvailableDateRepository.delete(checkExist);
                }

                calendar.add(Calendar.DATE, 1);
            }

            return new CustomResult(200, "Success", null);

        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    public CustomResult blockDate(int propertyId, String start, String end){
        try{
            var property = propertyRepository.findListingById(propertyId);

            if(property == null){
                return new CustomResult(404, "Property not found", null);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(start);
            Date endDate = dateFormat.parse(end);

            var checkList = bookingRepository.checkIfBlockable(propertyId, startDate, endDate);

            if(!checkList.isEmpty()){
                return new CustomResult(403, "Some one already book that day", checkList);
            }

            Calendar calendar = Calendar.getInstance();

            calendar.setTime(startDate);

            while (!calendar.getTime().after(endDate)) {
                var checkExist = propertyNotAvailableDateRepository.findByPropertyIdAndDate(propertyId, calendar.getTime());

                if(checkExist == null){
                    PropertyNotAvailableDate newPropertyNotAvailableDate = new PropertyNotAvailableDate();
                    newPropertyNotAvailableDate.setDate(calendar.getTime());
                    newPropertyNotAvailableDate.setProperty(property);
                    propertyNotAvailableDateRepository.save(newPropertyNotAvailableDate);
                }

                calendar.add(Calendar.DATE, 1);
            }

            return new CustomResult(200, "Success", null);

        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    public CustomResult changePriceForDates(int propertyId, String start, String end, double price){
        try{
            var property = propertyRepository.findListingById(propertyId);

            if(property == null){
                return new CustomResult(404, "Property not found", null);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(start);
            Date endDate = dateFormat.parse(end);

            var checkList = bookingRepository.checkIfBlockable(propertyId, startDate, endDate);

            if(!checkList.isEmpty()){
                return new CustomResult(403, "Some one already book that day", checkList);
            }

            Calendar calendar = Calendar.getInstance();

            calendar.setTime(startDate);

            while (!calendar.getTime().after(endDate)) {
                var checkExist = propertyExceptionDateRepository.findByPropertyIdAndDate(propertyId, calendar.getTime());

                if(checkExist != null){
                    checkExist.setBasePrice(price);
                    propertyExceptionDateRepository.save(checkExist);
                }

                if(checkExist == null){
                    PropertyExceptionDate newPropertyExceptionDate = new PropertyExceptionDate();
                    newPropertyExceptionDate.setDate(calendar.getTime());
                    newPropertyExceptionDate.setProperty(property);
                    newPropertyExceptionDate.setBasePrice(price);
                    propertyExceptionDateRepository.save(newPropertyExceptionDate);
                }

                calendar.add(Calendar.DATE, 1);
            }

            return new CustomResult(200, "Success", null);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

    public CustomResult getAllListingOfHost(String email){
        try{
            var user = userRepository.findUserByEmail(email);

            var listings = propertyRepository.findListingByUserId(user.getId());

            return new CustomResult(200, "Success", listings);

        } catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }


    // code giu

    // code giu
    public CustomResult getListingById(int id) {
        try {
            var property = propertyRepository.findListingById(id);

            if (property != null) {
                PropertyGiuDto propertyDto = new PropertyGiuDto();
                BeanUtils.copyProperties(property, propertyDto);
                propertyDto.setUser(property.getUser());

                if (property.getManagedCity() != null) {
                    propertyDto.setManagedCityId(property.getManagedCity().getId());
                }

                if (property.getRefundPolicy() != null) {
                    propertyDto.setRefundPolicyId(property.getRefundPolicy().getId());
                }

                if (property.getUser() != null) {
                    propertyDto.setUserId(property.getUser().getId());
                }

                if (property.getPropertyCategory() != null) {
                    propertyDto.setPropertyCategoryID(property.getPropertyCategory().getId());
                }

                if (property.getInstantBookRequirement() != null) {
                    propertyDto.setInstantBookRequirementID(property.getInstantBookRequirement().getId());
                }

                List<Integer> amenityList = new ArrayList<>();
                for (var amenity : property.getPropertyAmenities()) {
                    amenityList.add(amenity.getAmenity().getId());
                }
                propertyDto.setPropertyAmenities(amenityList);

                List<String> imageList = new ArrayList<>();
                for (var image : property.getPropertyImages()) {
                    imageList.add(image.getImageName());
                }
                propertyDto.setPropertyImages(imageList);

                // Lấy amenity
                List<Amenity> amenityListOb = property.getPropertyAmenities()
                        .stream()
                        .map(propertyAmenity -> propertyAmenity.getAmenity())
                        .collect(Collectors.toList());
                propertyDto.setAmenity(amenityListOb);
                // Lấy date thay đổi base-price
                List<PropertyExceptionDate> propertyExceptionDate = property.getPropertyExceptionDates()
                        .stream()
                        .map(propertyException -> propertyException)
                        .collect(Collectors.toList());
                propertyDto.setExceptionDates(propertyExceptionDate);
                // Lay date bi host block
                List<PropertyNotAvailableDate> propertyNotAvailableDates = property.getPropertyNotAvailableDates()
                        .stream()
                        .map(notAvailableDates -> notAvailableDates)
                        .collect(Collectors.toList());
                propertyDto.setNotAvailableDates(propertyNotAvailableDates);

                // Lấy danh sách các Booking theo propertyId
                List<Booking> bookings = property.getBookings();

                var listBookingAccepStatus = bookings.stream()
                        .filter((booking) -> booking.getStatus().equalsIgnoreCase("ACCEPT")
                                || booking.getStatus().equalsIgnoreCase("TRANSACTIONPENDDING"))
                        .toList();

                List<BookDateDetail> bookDateDetails = listBookingAccepStatus.stream()
                        .flatMap(booking -> booking.getBookDateDetails().stream())
                        .collect(Collectors.toList());
                propertyDto.setBookDateDetails(bookDateDetails);

                return new CustomResult(200, "Success", propertyDto);
            }
            return new CustomResult(404, "Not found", null);
        } catch (Exception e) {
            return new CustomResult(400, "Bad request", e.getMessage());
        }
    }
}
