package com.service.main.service;

import com.service.main.dto.CustomPaging;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PagingService {

    public CustomPaging convertToCustomPaging(Page<?> page, int pageNumber, int pageSize){
        var customPaging = new CustomPaging();
        customPaging.setStatus(200);
        customPaging.setMessage("Success");
        customPaging.setCurrentPage(pageNumber);
        customPaging.setPageSize(pageSize);
        customPaging.setTotalPages(page.getTotalPages());
        customPaging.setTotalCount(page.getTotalElements());
        customPaging.setData(page.getContent());

        return customPaging;
    }
}
