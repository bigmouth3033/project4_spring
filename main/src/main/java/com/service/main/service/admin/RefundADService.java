package com.service.main.service.admin;

import com.service.main.dto.CustomResult;
import com.service.main.repository.RefundPolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefundADService {

    @Autowired
    private RefundPolicyRepository refundPolicyRepository;


    public CustomResult getAllRefundPolicy(){
        try{

            var policies = refundPolicyRepository.findAll();

            return new CustomResult(200, "Success", policies);
        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }
}
