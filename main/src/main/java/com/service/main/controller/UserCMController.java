package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.dto.userDto.request.AddressRequest;
import com.service.main.dto.userDto.request.LegalNameRequest;
import com.service.main.dto.userDto.request.PhoneNumberRequest;
import com.service.main.dto.userDto.request.PreferredNameRequest;
import com.service.main.dto.userDto.response.AddressResponse;
import com.service.main.dto.userDto.response.LegalNameResponse;
import com.service.main.dto.userDto.response.PhoneNumberResponse;
import com.service.main.dto.userDto.response.PreferredNameResponse;
import com.service.main.entity.User;
import com.service.main.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("userCM")
public class UserCMController {
    @Autowired
    private UserRepository _userRepository;

    @PutMapping("legalName")
    public ResponseEntity<CustomResult> putLegalName(@ModelAttribute LegalNameRequest request) {
        CustomResult result = new CustomResult();
        try {
            // Change mock after
            String mockEmail = "bigmouth3033@gmail.com";
            var email = SecurityContextHolder.getContext().getAuthentication().getName();

            User user = _userRepository.findUserByEmail(email);
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user = _userRepository.save(user);


            LegalNameResponse response = new LegalNameResponse();
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());

            result.setStatus(200);
            result.setMessage("Update Legal Success");
            result.setData(response);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.setStatus(400);
            result.setMessage("Something went wrong while update LegalName");
            result.setData(null);
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("preferredName")
    public ResponseEntity<CustomResult> putPreferredName(@ModelAttribute PreferredNameRequest request) {
        CustomResult result = new CustomResult();
        try {
            // Change mock after
            String mockEmail = "bigmouth3033@gmail.com";
            var email = SecurityContextHolder.getContext().getAuthentication().getName();

            User user = _userRepository.findUserByEmail(email);
            user.setPreferredName(request.getPreferredName());
            user = _userRepository.save(user);

            PreferredNameResponse response = new PreferredNameResponse();
            response.setPreferredName(user.getPreferredName());

            result.setStatus(200);
            result.setMessage("Update Preferred Success");
            result.setData(response);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.setStatus(400);
            result.setMessage("Something went wrong while update LegalName");
            result.setData(null);
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("phoneNumber")
    public ResponseEntity<CustomResult> putPhoneNumber(@ModelAttribute PhoneNumberRequest request) {
        CustomResult result = new CustomResult();
        try {
            // Change mock after
            String mockEmail = "bigmouth3033@gmail.com";
            var email = SecurityContextHolder.getContext().getAuthentication().getName();

            User user = _userRepository.findUserByEmail(email);
            user.setPhoneNumber(request.getPhoneNumber());
            user = _userRepository.save(user);

            PhoneNumberResponse phoneNumberResponse = new PhoneNumberResponse();
            phoneNumberResponse.setPhoneNumber(user.getPhoneNumber());

            result.setStatus(200);
            result.setMessage("Update Preferred Success");
            result.setData(phoneNumberResponse);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.setStatus(400);
            result.setMessage("Something went wrong while update LegalName");
            result.setData(null);
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("address")
    public ResponseEntity<CustomResult> putAddress(@ModelAttribute AddressRequest request) {
        CustomResult result = new CustomResult();
        try {
            var email = SecurityContextHolder.getContext().getAuthentication().getName();

            User user = _userRepository.findUserByEmail(email);
            user.setAddress(request.getAddress());
            _userRepository.save(user);

            AddressResponse addressResponse = new AddressResponse();
            addressResponse.setAddress(user.getAddress());

            result.setStatus(200);
            result.setMessage("Update Preferred Success");
            result.setData(addressResponse);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.setStatus(400);
            result.setMessage("Something went wrong while update Address");
            result.setData(null);
            return ResponseEntity.badRequest().body(result);
        }
    }
}
