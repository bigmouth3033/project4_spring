package com.service.main.controller;

import com.service.main.dto.ChangeRoleDto;
import com.service.main.dto.CustomResult;
import com.service.main.service.admin.RoleADService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("roleAD")
public class RoleADController {

    @Autowired
    private RoleADService roleADService;

    @GetMapping("get_roles")
    @RolesAllowed({"ADMIN", "EMPLOYEE"})
    public ResponseEntity<CustomResult> getRoles(){
        var customResult = roleADService.getAllRoles();
        return ResponseEntity.ok(customResult);
    }

    @PutMapping("change_employee_role")
    @RolesAllowed({"ADMIN","EMPLOYEE_MANAGEMENT"})
    public ResponseEntity<CustomResult> changeEmployeeRole(@ModelAttribute ChangeRoleDto changeRoleDto){
        var customResult = roleADService.changeEmployeeRole(changeRoleDto);
        return ResponseEntity.ok(customResult);
    }
}
