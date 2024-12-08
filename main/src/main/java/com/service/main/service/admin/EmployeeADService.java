package com.service.main.service.admin;

import com.service.main.dto.*;
import com.service.main.entity.*;
import com.service.main.repository.*;
import com.service.main.service.ImageUploadingService;
import com.service.main.service.MailBodyService;
import com.service.main.service.MailService;
import com.service.main.service.PagingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeADService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ImageUploadingService imageUploadingService;

    @Autowired
    private AdminRoleRepository adminRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PagingService pagingService;

    @Autowired
    private MailService mailService;

    @Autowired
    private ManagedCityRepository managedCityRepository;

    @Autowired
    private AdminManageCityRepository adminManageCityRepository;

    @Autowired
    private MailBodyService mailBodyService;

    public CustomPaging getEmployeeList(int pageNumber, int pageSize,Boolean status, String searchText,List<Integer> cityFilter, List<Integer> roleFilter) {
        try{
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updatedAt").descending());

            Page<Admin> employeeList = adminRepository.findEmployee(status, searchText,cityFilter, roleFilter , pageable);

            var list = employeeList.getContent().stream().map(employee -> {
                var adminDto = new AdminDto();
                BeanUtils.copyProperties(employee, adminDto);

                var roleList = new ArrayList<RoleDto>();

                for(var roleObj : employee.getAdminRoles()){
                    var roleDto = new RoleDto();
                    roleDto.setId(roleObj.getRole().getId());
                    roleDto.setRoleName(roleObj.getRole().getRoleName());
                    roleList.add(roleDto);
                }

                adminDto.setRoles(roleList);

                var cityList = new ArrayList<ManagedCityDto>();

                for(var cityObj : employee.getAdminManageCities()){
                    var cityDto = new ManagedCityDto();
                    cityDto.setId(cityObj.getManagedCity().getId());
                    cityDto.setCityName(cityObj.getManagedCity().getCityName());
                    cityList.add(cityDto);
                }

                adminDto.setCities(cityList);
                return adminDto;
            }).collect(Collectors.toList());

            Page<AdminDto> updatedPage = new PageImpl<>(list, pageable, employeeList.getTotalElements());

            return pagingService.convertToCustomPaging(updatedPage, pageNumber, pageSize);
        }catch (Exception e){
            var customPaging = new CustomPaging();
            customPaging.setStatus(400);
            customPaging.setMessage(e.getMessage());
            return customPaging;
        }
    }

    @Transactional
    public CustomResult updateEmployee(UpdateEmployeeDto updateEmployeeDto){
        try{
            var employee = adminRepository.findById(updateEmployeeDto.getId());

            if(employee.isEmpty()){
                return new CustomResult(404, "Not found", null);
            }

            employee.get().setFirstName(updateEmployeeDto.getFirstName());
            employee.get().setLastName(updateEmployeeDto.getLastName());
            employee.get().setAddress(updateEmployeeDto.getAddress());

            if(!employee.get().getPhoneNumber().equals(updateEmployeeDto.getPhoneNumber())){
                var isPhoneNumberExist = adminRepository.findByPhoneNumber(updateEmployeeDto.getPhoneNumber());

                if(isPhoneNumberExist != null){
                    return new CustomResult(403, "Phone number already exist", null);
                }

                employee.get().setPhoneNumber(updateEmployeeDto.getPhoneNumber());
            }

            if(updateEmployeeDto.getAvatar() != null){
                employee.get().setAvatar(imageUploadingService.upload(updateEmployeeDto.getAvatar()));
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            employee.get().setDob(sdf.parse(updateEmployeeDto.getDob()));

            if(updateEmployeeDto.getRoleIds() == null){
                updateEmployeeDto.setRoleIds(new ArrayList<Integer>());
            }

            for (var empRole : updateEmployeeDto.getRoleIds()) {
                var role = roleRepository.findById(empRole);
                var id = new AdminRoleId(updateEmployeeDto.getId(), empRole);
                var adminRole = adminRoleRepository.findById(id);

                if (adminRole.isEmpty()) {
                    var newAdminRole = new AdminRole();
                    newAdminRole.setAdminRoleId(id);
                    newAdminRole.setAdmin(employee.get());
                    newAdminRole.setRole(role.get());
                    adminRoleRepository.save(newAdminRole);
                }
            }

            adminRoleRepository.deleteEmployeeRole(updateEmployeeDto.getId(), updateEmployeeDto.getRoleIds());

            if(updateEmployeeDto.getCityIds() == null){
                updateEmployeeDto.setCityIds(new ArrayList<Integer>());
            }


            for (var city : updateEmployeeDto.getCityIds()) {
                var managedCity = managedCityRepository.findById(city);
                var id = new AdminManageCityId(updateEmployeeDto.getId(), city);
                var userManagedCity = adminManageCityRepository.findById(id);

                if (userManagedCity.isEmpty()) {

                    var newUserManagedCity = new AdminManageCity();
                    newUserManagedCity.setId(id);
                    newUserManagedCity.setAdmin(employee.get());
                    newUserManagedCity.setManagedCity(managedCity.get());

                    adminManageCityRepository.save(newUserManagedCity);
                }
            }


            adminManageCityRepository.deleteManagedCity(updateEmployeeDto.getId(), updateEmployeeDto.getCityIds());


            return new CustomResult(200, "Success", null);

        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    public CustomResult readEmployeeById(int employeeId) {
        try{
            var employee = adminRepository.findById(employeeId);

            if(employee.isEmpty()){
                return new CustomResult(404, "Not found", null);
            }

            var adminDto = new AdminDto();
            BeanUtils.copyProperties(employee.get(), adminDto);

            var roleList = new ArrayList<RoleDto>();

            for(var roleObj : employee.get().getAdminRoles()){

                if(roleObj.getRole().getRoleName().equals("ADMIN")){
                    return new CustomResult(404, "Not found", null);
                }

                var roleDto = new RoleDto();
                roleDto.setId(roleObj.getRole().getId());
                roleDto.setRoleName(roleObj.getRole().getRoleName());
                roleList.add(roleDto);
            }

            adminDto.setRoles(roleList);

            var cityList = new ArrayList<ManagedCityDto>();

            for(var cityObj : employee.get().getAdminManageCities()){
                var cityDto = new ManagedCityDto();
                cityDto.setId(cityObj.getManagedCity().getId());
                cityDto.setCityName(cityObj.getManagedCity().getCityName());
                cityList.add(cityDto);
            }

            adminDto.setCities(cityList);

            return new CustomResult(200, "OK", adminDto);

        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    public CustomResult changeEmployeeStatus(int employeeId){
        try{
            var employee = adminRepository.findById(employeeId);

            if(employee.isEmpty()){
                return new CustomResult(404, "Employee not found", null);
            }

            employee.get().setStatus(!employee.get().isStatus());

            adminRepository.save(employee.get());

            return new CustomResult(200, "Success", null);

        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    public CustomResult createNewEmployee(CreateEmployeeDto createEmployeeDto){
        try{
            var checkEmailExist = adminRepository.findByEmail(createEmployeeDto.getEmail());

            if(checkEmailExist != null){
                return new CustomResult(403, "Employee email already exist!", null);
            }

            var checkPhoneExist = adminRepository.findByPhoneNumber(createEmployeeDto.getPhoneNumber());

            if(checkPhoneExist != null){
                return new CustomResult(403, "Employee phone number already exist!", null);
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            var newEmployee = new Admin();
            newEmployee.setEmail(createEmployeeDto.getEmail());
            newEmployee.setPhoneNumber(createEmployeeDto.getPhoneNumber());
            newEmployee.setFirstName(createEmployeeDto.getFirstName());
            newEmployee.setLastName(createEmployeeDto.getLastName());
            newEmployee.setPassword(passwordEncoder.encode(createEmployeeDto.getPassword()));
            newEmployee.setDob(format.parse(createEmployeeDto.getDob()));
            newEmployee.setAddress(createEmployeeDto.getAddress());
            newEmployee.setAvatar(imageUploadingService.upload(createEmployeeDto.getAvatar()));
            newEmployee.setStatus(true);

            adminRepository.save(newEmployee);

            var newEmployeeRole = new AdminRole();

            var role = roleRepository.findRoleByRoleName("EMPLOYEE");

            if(createEmployeeDto.getRoleIds() != null && !createEmployeeDto.getRoleIds().isEmpty()){
                for(var roleID :  createEmployeeDto.getRoleIds()){
                    var empRole = roleRepository.findById(roleID);

                    var newRole = new AdminRole();
                    newRole.setAdminRoleId(new AdminRoleId(newEmployee.getId(), roleID));
                    newRole.setAdmin(newEmployee);
                    newRole.setRole(empRole.get());

                    adminRoleRepository.save(newRole);
                }
            }

            if(createEmployeeDto.getCityIds() != null && !createEmployeeDto.getCityIds().isEmpty()){
                for(var cityID :  createEmployeeDto.getCityIds()){
                    var city = managedCityRepository.findById(cityID);

                    var newManagedCity = new AdminManageCity();
                    newManagedCity.setId(new AdminManageCityId(newEmployee.getId(), cityID));
                    newManagedCity.setAdmin(newEmployee);
                    newManagedCity.setManagedCity(city.get());

                    adminManageCityRepository.save(newManagedCity);
                }
            }

            newEmployeeRole.setAdminRoleId(new AdminRoleId(newEmployee.getId(), role.getId()));
            newEmployeeRole.setRole(role);
            newEmployeeRole.setAdmin(newEmployee);
            adminRoleRepository.save(newEmployeeRole);

            mailService.sendMail(null, newEmployee.getEmail(), new String[]{newEmployee.getEmail()}, "Welcome new employee", mailBodyService.getEmployeeWelcomeMail(newEmployee.getFirstName(),newEmployee.getEmail(), createEmployeeDto.getPassword()));

            return new CustomResult(200, "Employee created", null);
        }catch (Exception ex){
            return new CustomResult(400, ex.getMessage(), null);
        }
    }
}
