package com.service.main.service;

import com.service.main.repository.AuthenticationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ScheduleService {

    @Autowired
    private AuthenticationCodeRepository authenticationCodeRepository;

    @Transactional
    @Scheduled(cron = "0 0 1 1 * ?")
    public void cleanupAuthenticationCode() {
        Date today = new Date();
        long yesterdayMillis = today.getTime() - (24 * 60 * 60 * 1000);
        Date yesterday = new Date(yesterdayMillis);
        authenticationCodeRepository.deleteExpiredCode(yesterday);
    }
}
