package com.service.main.controller;

import com.service.main.dto.CustomPaging;
import com.service.main.dto.CustomResult;
import com.service.main.dto.MailDto;
import com.service.main.service.admin.MailADService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("mailAD")
public class MailADController {

    @Autowired
    private MailADService mailADService;

    @GetMapping("user_list")
    @RolesAllowed({"ADMIN", "MAIL_MANAGEMENT"})
    public ResponseEntity<CustomResult> getUserListForMail(){
        var customResult = mailADService.getUserMailList();
        return ResponseEntity.ok(customResult);
    }


    @PostMapping("create_new_mail")
    @RolesAllowed({"ADMIN", "MAIL_MANAGEMENT"})
    public ResponseEntity<CustomResult> createNewEmail(@ModelAttribute MailDto mailDto){
        var  customResult = mailADService.createNewEmail(mailDto);
        return ResponseEntity.ok(customResult);
    }

    @PostMapping("edit_schedule_mail")
    @RolesAllowed({"ADMIN", "MAIL_MANAGEMENT"})
    public ResponseEntity<CustomResult> editScheduleMail(@ModelAttribute MailDto mailDto){
        var  customResult = mailADService.editScheduleMail(mailDto);
        return ResponseEntity.ok(customResult);
    }

    @GetMapping("get_mail_list")
    @RolesAllowed({"ADMIN", "MAIL_MANAGEMENT"})
    public ResponseEntity<CustomPaging> getEmailList(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam(required = false, defaultValue = "") String searchHeader, @RequestParam String status){
        var  customPaging = mailADService.getMailList(pageNumber, pageSize, searchHeader, status);
        return ResponseEntity.ok(customPaging);
    }

    @GetMapping("get_mail_by_id")
    @RolesAllowed({"ADMIN", "MAIL_MANAGEMENT"})
    public ResponseEntity<CustomResult> getMailById(@RequestParam int id){
        var customResult = mailADService.getMailById(id);
        return ResponseEntity.ok(customResult);
    }

}
