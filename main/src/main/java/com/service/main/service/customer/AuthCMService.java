package com.service.main.service.customer;

import com.service.main.dto.CustomResult;
import com.service.main.dto.LoginDto;
import com.service.main.dto.RegisterDto;
import com.service.main.entity.AuthenticationCode;
import com.service.main.entity.User;
import com.service.main.repository.AuthenticationCodeRepository;
import com.service.main.repository.UserRepository;

import com.service.main.service.JwtService;
import com.service.main.service.MailBodyService;
import com.service.main.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Service
public class AuthCMService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailBodyService mailBodyService;

    @Autowired
    private AuthenticationCodeRepository authenticationCodeRepository;

    @Autowired
    private JwtService jwtService;

    public CustomResult login(LoginDto loginDto) {
        try{
            var user = userRepository.findUserByEmail(loginDto.getEmail());

            if(user != null && passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
                var token = jwtService.generateToken(new HashMap<>(), loginDto.getEmail(), "user");

                return new CustomResult(200, "Success", token);
            }

            return new CustomResult(403, "Invalid login credentials. Please try again.", null);
        }catch (Exception e){
            return new CustomResult(400, "Bad request", null);
        }
    }


    public CustomResult userRegister(RegisterDto registerDto) {
        try{

            var authenticationCode = authenticationCodeRepository.findByCodeAndEmail(registerDto.getCode(), registerDto.getEmail());

            if(authenticationCode == null){
                return new CustomResult(403, "This code does not exist", null);
            }

            int comparison = authenticationCode.getExpiredTime().compareTo(new Date());

            if(comparison < 0){
                return new CustomResult(403, "Code is expired", null);
            }

            var newUser = new User();
            newUser.setFirstName(registerDto.getFirstName());
            newUser.setLastName(registerDto.getLastName());
            newUser.setEmail(registerDto.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-d");
            newUser.setDob(formatter.parse(registerDto.getDob()));
            newUser.setStatus(true);
            newUser.setVerified(true);
            userRepository.save(newUser);
            return new CustomResult(200, "Success", newUser);
        } catch (Exception ex) {
            return new CustomResult(400, "Bad request" , ex.getMessage());
        }
    }

    public CustomResult createAuthenticationCode(String email) {
        try{
            var account = userRepository.findUserByEmail(email);

            if(account != null){
                return new CustomResult(403, "This email already exists", null);
            }

            var newAuthenticationCode = new AuthenticationCode();
            newAuthenticationCode.setEmail(email);
            newAuthenticationCode.setExpiredTime(new Date(System.currentTimeMillis() + 1000 * 60 * 10));
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[4];
            secureRandom.nextBytes(randomBytes);
            newAuthenticationCode.setCode(Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0, 6));

            mailService.sendMail(null, email, new String[]{email}, "Authentication code", mailBodyService.getRegisterAuthenticationMail(newAuthenticationCode.getCode()));

            authenticationCodeRepository.save(newAuthenticationCode);

            return new CustomResult(200, "Success", null);
        }catch (Exception ex) {
            return new CustomResult(400, "Bad request" , ex.getMessage());
        }
    }


    public CustomResult checkEmailExist(String email) {
        try{
            var user = userRepository.findUserByEmail(email);

            if(user != null) {
                return new CustomResult(200, "Success", true);
            }

            return new CustomResult(200, "Success", false);

        }catch (Exception ex){
            return new CustomResult(400, "Bad request", null);
        }
    }

    public CustomResult getUser(String email){
        try{
            var user = userRepository.findUserByEmail(email);
            if(user != null){
                return new CustomResult(200, "Success", user);
            }

            return new CustomResult(404, "User not found", null);
        }catch (Exception ex){
            return new CustomResult(400, "Bad request", null);
        }
    }

}
