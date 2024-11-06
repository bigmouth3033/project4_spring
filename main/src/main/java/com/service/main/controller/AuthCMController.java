package com.service.main.controller;

import com.service.main.dto.CustomResult;
import com.service.main.dto.LoginDto;
import com.service.main.dto.RegisterDto;
import com.service.main.service.customer.AuthCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("authCM")
public class AuthCMController {

    @Autowired
    private AuthCMService authCMService;


    @GetMapping
    public ResponseEntity<CustomResult> user(){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var customResult = authCMService.getUser(email);
        return ResponseEntity.ok(customResult);
    }

    @PostMapping("login_signup")
    public ResponseEntity<CustomResult> checkLoginOrSignUp(String email){
        var customResult = authCMService.checkEmailExist(email);
        return ResponseEntity.ok(customResult);
    }

    @PostMapping("create_authentication_code")
    public ResponseEntity<CustomResult> createAuthenticationCode(String email){
        var customResult = authCMService.createAuthenticationCode(email);
        return ResponseEntity.ok(customResult);
    }

    @PostMapping("register")
    public ResponseEntity<CustomResult> register(RegisterDto registerDto){
        var customResult = authCMService.userRegister(registerDto);
        return ResponseEntity.ok(customResult);
    }

    @PostMapping("login")
    public ResponseEntity<CustomResult> login(LoginDto loginDto){
        var customResult = authCMService.login(loginDto);
        return ResponseEntity.ok(customResult);
    }
}
