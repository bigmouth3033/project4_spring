package com.service.main.service.admin;

import com.service.main.dto.AdminBookingDto;
import com.service.main.dto.CustomPaging;
import com.service.main.dto.PropertyAdminDto;
import com.service.main.repository.AdminRepository;
import com.service.main.repository.BookingRepository;
import com.service.main.repository.RoleRepository;
import com.service.main.service.PagingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BookingADService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PagingService pagingService;


    public CustomPaging getBookingList(String email,
                                       int pageNumber,
                                       int pageSize,
                                       String hostSearch,
                                       String customerSearch,
                                       String bookingType,
                                       String startDate,
                                       String endDate,
                                       List<Integer> locationIds,
                                       String status,
                                       String propertySearch,
                                       List<Integer> refundIds
    ) {
        try {

            var employee = adminRepository.findByEmail(email);

            if (employee == null) {
                var customPaging = new CustomPaging();
                customPaging.setMessage("Employee not found");
                customPaging.setStatus(403);
                return customPaging;
            }

            boolean isAdmin = false;

            for (var empRole : employee.getAdminRoles()) {
                var role = roleRepository.findById(empRole.getRole().getId());

                if (role.get().getRoleName().equals("ADMIN")) {
                    isAdmin = true;
                    break;
                }
            }

            List<Integer> employeeManagedCity = employee.getAdminManageCities().stream().map(city -> city.getManagedCity().getId()).toList();

            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updatedAt").descending());

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

            Date start = sf.parse(startDate);
            Date end = sf.parse(endDate);


            Integer propertyIdSearch = null;
            String propertyNameSearch = null;

            if(propertySearch != null) {
                try{
                    propertyIdSearch = Integer.parseInt(propertySearch);
                }catch (Exception e){
                    propertyNameSearch = propertySearch;
                }

            }else{
                propertyNameSearch = propertySearch;
            }



            var bookingList = bookingRepository.getAdminBookingList(
                    isAdmin,
                    employeeManagedCity,
                    status,
                    hostSearch,
                    customerSearch,
                    bookingType,
                    start,
                    end,
                    locationIds,
                    propertyNameSearch,
                    propertyIdSearch,
                    refundIds,
                    pageable
            );

            List<AdminBookingDto> list = bookingList.getContent().stream().map((booking) -> {
                var adminBookingDto = new AdminBookingDto();
                BeanUtils.copyProperties(booking, adminBookingDto);

                adminBookingDto.setPropertyImage(booking.getProperty().getPropertyImages().getFirst().getImageName());
                adminBookingDto.setPropertyName(booking.getProperty().getPropertyTitle());

                adminBookingDto.setPropertyCity(booking.getProperty().getManagedCity().getCityName());

                return adminBookingDto;

            }).toList();

            Page<AdminBookingDto> updatedPage = new PageImpl<>(list, pageable, bookingList.getTotalElements());

            return pagingService.convertToCustomPaging(updatedPage, pageNumber, pageSize);


        } catch (Exception e) {
            var customPaging = new CustomPaging();
            customPaging.setStatus(400);
            customPaging.setMessage(e.getMessage());
            return customPaging;
        }
    }
}
