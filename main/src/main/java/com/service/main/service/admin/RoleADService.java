package com.service.main.service.admin;

import com.service.main.dto.ChangeRoleDto;
import com.service.main.dto.CustomResult;
import com.service.main.entity.AdminManageCity;
import com.service.main.entity.AdminManageCityId;
import com.service.main.entity.AdminRole;
import com.service.main.entity.AdminRoleId;
import com.service.main.repository.AdminRepository;
import com.service.main.repository.AdminRoleRepository;
import com.service.main.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class RoleADService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminRoleRepository adminRoleRepository;



    public CustomResult getAllRoles() {
        try{
            var roles = roleRepository.findAll();

            return new CustomResult(200, "Success", roles);
        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    @Transactional
    public CustomResult changeEmployeeRole(ChangeRoleDto changeRoleDto) {
        try{
            var employee = adminRepository.findById(changeRoleDto.getUserId());

            if (employee.isEmpty()) {
                return new CustomResult(404, "Not found", null);
            }

            if(changeRoleDto.getRoleIds() == null){
                changeRoleDto.setRoleIds(new ArrayList<Integer>());
            }

            for (var empRole : changeRoleDto.getRoleIds()) {
                var role = roleRepository.findById(empRole);
                var id = new AdminRoleId(changeRoleDto.getUserId(), empRole);
                var adminRole = adminRoleRepository.findById(id);

                if (adminRole.isEmpty()) {

                    var newAdminRole = new AdminRole();
                    newAdminRole.setAdminRoleId(id);
                    newAdminRole.setAdmin(employee.get());
                    newAdminRole.setRole(role.get());

                    adminRoleRepository.save(newAdminRole);
                }
            }


            adminRoleRepository.deleteEmployeeRole(changeRoleDto.getUserId(), changeRoleDto.getRoleIds());


            return new CustomResult(200, "OK", null);


        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }
}
