package com.service.main.service.admin;

import com.service.main.dto.*;
import com.service.main.entity.AdminManageCity;
import com.service.main.entity.AdminManageCityId;
import com.service.main.entity.ManagedCity;
import com.service.main.repository.AdminManageCityRepository;
import com.service.main.repository.AdminRepository;
import com.service.main.repository.ManagedCityRepository;
import com.service.main.service.PagingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityADService {

    @Autowired
    private ManagedCityRepository managedCityRepository;

    @Autowired
    private PagingService pagingService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminManageCityRepository adminManageCityRepository;

    public CustomResult getCities() {
        try {
            var cities = managedCityRepository.findAll();

            List<ManagedCityDto> cityDtoList = cities.stream().map(city -> {
                var cityDto = new ManagedCityDto();
                BeanUtils.copyProperties(city, cityDto);
                return cityDto;
            }).toList();

            return new CustomResult(200, "Success", cityDtoList);

        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    public CustomResult changeCityStatus(ChangeCityStatusDto changeCityStatusDto) {
        try {
            var city = managedCityRepository.findById(changeCityStatusDto.getId());

            if (city.isEmpty()) {
                return new CustomResult(404, "Not found", null);
            }

            city.get().setManaged(changeCityStatusDto.isStatus());
            managedCityRepository.save(city.get());

            return new CustomResult(200, "OK", null);
        } catch (Exception e) {
            return new CustomResult(400, "Bad request", null);
        }
    }


    public CustomPaging getCityList(int pageNumber, int pageSize, String cityName, String status) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));

            Page<ManagedCity> pagedCity = null;

            if (status.equals("true")) {
                 pagedCity = managedCityRepository.findCity(cityName, true, pageable);
            }

            if (status.equals("false")) {
                 pagedCity = managedCityRepository.findCity(cityName, false, pageable);
            }

            if(!status.equals("true") && !status.equals("false")) {
                pagedCity = managedCityRepository.findCity(cityName, null, pageable);
            }

            List<ManagedCityDto> managedCityDtoList = pagedCity.getContent().stream().map(city -> {
                ManagedCityDto managedCityDto = new ManagedCityDto();
                BeanUtils.copyProperties(city, managedCityDto);
                managedCityDto.setPropertyCount(city.getProperties().size());
                return managedCityDto;
            }).toList();

            Page<ManagedCityDto> updatedPage = new PageImpl<>(managedCityDtoList, pageable, pagedCity.getTotalElements());
            return pagingService.convertToCustomPaging(updatedPage, pageNumber, pageSize);

        } catch (Exception e) {
            return new CustomPaging();
        }
    }

    @Transactional
    public CustomResult changeUserManagedCity(ChangeManagedCityDto changeManagedCityDto) {
        try {
            var employee = adminRepository.findById(changeManagedCityDto.getUserId());

            if (employee.isEmpty()) {
                return new CustomResult(404, "Not found", null);
            }

            if(changeManagedCityDto.getCityId() == null){
                changeManagedCityDto.setCityId(new ArrayList<Integer>());
            }


            for (var city : changeManagedCityDto.getCityId()) {
                var managedCity = managedCityRepository.findById(city);
                var id = new AdminManageCityId(changeManagedCityDto.getUserId(), city);
                var userManagedCity = adminManageCityRepository.findById(id);

                if (userManagedCity.isEmpty()) {

                    var newUserManagedCity = new AdminManageCity();
                    newUserManagedCity.setId(id);
                    newUserManagedCity.setAdmin(employee.get());
                    newUserManagedCity.setManagedCity(managedCity.get());

                    adminManageCityRepository.save(newUserManagedCity);
                }
            }


            adminManageCityRepository.deleteManagedCity(changeManagedCityDto.getUserId(), changeManagedCityDto.getCityId());


            return new CustomResult(200, "OK", null);


        } catch (Exception e) {
            return new CustomResult(400, e.getMessage(), null);
        }
    }
}
