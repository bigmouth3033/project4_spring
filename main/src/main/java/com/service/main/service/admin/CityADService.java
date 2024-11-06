package com.service.main.service.admin;

import com.service.main.dto.ChangeCityStatusDto;
import com.service.main.dto.CustomPaging;
import com.service.main.dto.CustomResult;
import com.service.main.repository.ManagedCityRepository;
import com.service.main.service.PagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CityADService {

    @Autowired
    private ManagedCityRepository managedCityRepository;

    @Autowired
    private PagingService pagingService;

    public CustomResult changeCityStatus(ChangeCityStatusDto changeCityStatusDto){
        try{
            var city = managedCityRepository.findById(changeCityStatusDto.getId());

            if(city.isEmpty()){
                return new CustomResult(404, "Not found", null);
            }

            city.get().setManaged(changeCityStatusDto.isStatus());
            managedCityRepository.save(city.get());

            return new CustomResult(200, "OK", null);
        }catch (Exception e){
            return new CustomResult(400, "Bad request", null);
        }
    }


    public CustomPaging getCityList(int pageNumber, int pageSize, String cityName, String status) {
        try{
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
            if(status.equals("true")){
                var pagedCity = managedCityRepository.findCity(cityName, true, pageable);
                return pagingService.convertToCustomPaging(pagedCity, pageNumber, pageSize);
            }

            if(status.equals("false")){
                var pagedCity = managedCityRepository.findCity(cityName, false, pageable);
                return pagingService.convertToCustomPaging(pagedCity, pageNumber, pageSize);
            }

            var pagedCity = managedCityRepository.findCity(cityName, null, pageable);
            return pagingService.convertToCustomPaging(pagedCity, pageNumber, pageSize);

        }catch (Exception e){
            return new CustomPaging();
        }
    }
}
