package com.service.main.service.customer;

import com.service.main.dto.CustomResult;
import com.service.main.repository.NotificationRepository;
import com.service.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationCMService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public CustomResult getUserPopUpNotification(String email) {
        try{
            var user = userRepository.findUserByEmail(email);

            if(user == null) {
                return new CustomResult(404, "Not found", null);
            }

            var notifications = notificationRepository.findUserPopUpNotification(user.getId());

            return new CustomResult(200, "Success", notifications);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", ex.getMessage());
        }
    }

}
