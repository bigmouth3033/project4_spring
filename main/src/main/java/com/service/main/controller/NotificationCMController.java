package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.service.customer.NotificationCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notificationCM")
public class NotificationCMController {

    @Autowired
    private NotificationCMService notificationCMService;

    @GetMapping("get_notification_popup")
    public ResponseEntity<CustomResult> getNotificationPopup() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var customResult = notificationCMService.getUserPopUpNotification(email);
        return ResponseEntity.ok(customResult);
    }
}
