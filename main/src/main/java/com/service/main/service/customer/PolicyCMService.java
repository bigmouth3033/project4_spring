package com.service.main.service.customer;

import com.service.main.dto.CustomResult;
import com.service.main.repository.RefundPolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PolicyCMService {


    @Autowired
    private RefundPolicyRepository refundPolicyRepository;


    public CustomResult getServices(){
        try{
            var refunds = refundPolicyRepository.findAll();

            return new CustomResult(200, "Success", refunds);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }
}
