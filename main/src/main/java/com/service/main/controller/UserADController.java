package com.service.main.controller;

import com.service.main.dto.CustomPaging;
import com.service.main.service.admin.UserADService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("userAD")
public class UserADController {

    @Autowired
    private UserADService userADService;

    @GetMapping("get_roles")
    @RolesAllowed({"ADMIN", "USER_MANAGEMENT"})
    public ResponseEntity<CustomPaging> getUsers(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam(required = false) String searchText, @RequestParam(required = false) List<Integer> badges, @RequestParam(required = false) String userType) {
        var customPaging = userADService.getUserList(pageNumber, pageSize, searchText, badges, userType);
        return ResponseEntity.ok(customPaging);
    }
}
