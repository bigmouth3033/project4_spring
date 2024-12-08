package com.service.main.controller;

import com.service.main.dto.CreateEmployeeDto;
import com.service.main.dto.CustomPaging;
import com.service.main.dto.CustomResult;
import com.service.main.dto.UpdateEmployeeDto;
import com.service.main.service.admin.EmployeeADService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("employeeAD")
public class EmployeeADController {
    @Autowired
    private EmployeeADService employeeADService;

    @RolesAllowed({"ADMIN", "EMPLOYEE_MANAGEMENT"})
    @PostMapping("create_employee")
    public ResponseEntity<CustomResult> createEmployee(@ModelAttribute CreateEmployeeDto createEmployeeDto) {
        var customResult = employeeADService.createNewEmployee(createEmployeeDto);
        return ResponseEntity.ok(customResult);
    }

    @RolesAllowed({"ADMIN", "EMPLOYEE_MANAGEMENT"})
    @GetMapping("get_employees")
    public ResponseEntity<CustomPaging> getEmployee(@RequestParam("pageNumber") int pageNumber,
                                                    @RequestParam("pageSize") int pageSize,
                                                    @RequestParam(value = "status", required = false) Boolean status, @RequestParam("searchText") String searchText,
                                                    @RequestParam(value = "cityFilter",required = false) List<Integer> cityFilter,
                                                    @RequestParam(value = "roleFilter",required = false) List<Integer> roleFilter) {
        var customPaging = employeeADService.getEmployeeList(pageNumber, pageSize, status, searchText, cityFilter, roleFilter);
        return ResponseEntity.ok(customPaging);
    }

    @RolesAllowed({"ADMIN", "EMPLOYEE_MANAGEMENT"})
    @PutMapping("change_status")
    public ResponseEntity<CustomResult> getEmployee(int employeeId) {
        var customResult = employeeADService.changeEmployeeStatus(employeeId);
        return ResponseEntity.ok(customResult);
    }

    @RolesAllowed({"ADMIN", "EMPLOYEE_MANAGEMENT"})
    @GetMapping("get_employee_by_id")
    public ResponseEntity<CustomResult> getEmployeeById(@RequestParam("employeeId") int employeeId) {
        var customResult = employeeADService.readEmployeeById(employeeId);
        return ResponseEntity.ok(customResult);
    }

    @RolesAllowed({"ADMIN", "EMPLOYEE_MANAGEMENT"})
    @PutMapping("update_employee")
    public ResponseEntity<CustomResult> updateEmployee(@ModelAttribute UpdateEmployeeDto updateEmployeeDto) {
        var customResult = employeeADService.updateEmployee(updateEmployeeDto);
        return ResponseEntity.ok(customResult);
    }
}
