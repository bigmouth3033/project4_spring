package com.service.main.service.admin;

import com.service.main.dto.*;
import com.service.main.repository.AdminRepository;
import com.service.main.service.JwtService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthADService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public CustomResult admin(String email){
        try{
            var admin = adminRepository.findByEmail(email);

            if(admin == null){
                return new CustomResult(404, "Not found", null);
            }

            var adminDto = new AdminDto();

            BeanUtils.copyProperties(admin, adminDto);

            var roleList = new ArrayList<RoleDto>();

            for(var roleObj : admin.getAdminRoles()){
                var roleDto = new RoleDto();
                roleDto.setId(roleObj.getRole().getId());
                roleDto.setRoleName(roleObj.getRole().getRoleName());
                roleList.add(roleDto);
            }

            adminDto.setRoles(roleList);

            var cityList = new ArrayList<ManagedCityDto>();

            for(var cityObj : admin.getAdminManageCities()){
                var cityDto = new ManagedCityDto();
                cityDto.setId(cityObj.getManagedCity().getId());
                cityDto.setCityName(cityObj.getManagedCity().getCityName());
                cityList.add(cityDto);
            }
            
            adminDto.setCities(cityList);

            return new CustomResult(200, "OK", adminDto);
        }catch (Exception e){
            return new CustomResult(400, "Bad request", null);
        }
    }


    public CustomResult login(LoginDto loginDto) {
        try{
            var admin = adminRepository.findByEmail(loginDto.getEmail());

            if(admin != null && passwordEncoder.matches(loginDto.getPassword(), admin.getPassword())){

                if(!admin.isStatus()){
                    return new CustomResult(403, "Account is not active", null);
                }

                var roleList = new ArrayList<String>();

                for(var roleObj : admin.getAdminRoles()){
                    roleList.add(roleObj.getRole().getRoleName());
                }
                Map<String, Object> claims = new HashMap<>();
                claims.put("roles", roleList);
                var token = jwtService.generateToken(claims, admin.getEmail(), "admin");
                return new CustomResult(200, "Success", token);
            }

            return new CustomResult(404, "Not found", null);

        }catch (Exception ex){
            return new CustomResult(400, "Bad request", null);
        }
    }
}
