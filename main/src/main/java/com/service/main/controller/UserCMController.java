package com.service.main.controller;

<<<<<<< HEAD
import com.service.main.dto.CustomResult;
import com.service.main.dto.UserInfoDto;
import com.service.main.service.customer.UserCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
=======
import com.google.storage.v2.CustomerEncryption;
import com.service.main.dto.CustomResult;
import com.service.main.dto.userDto.request.*;
import com.service.main.dto.userDto.response.*;
import com.service.main.entity.User;
import com.service.main.repository.UserRepository;
import com.service.main.service.ImageUploadingService;
import com.service.main.service.MailService;
import com.service.main.service.OTPGenerator;
import com.service.main.service.azure.AzureSender;
import com.service.main.service.azure.models.MailPayload;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
>>>>>>> 190256b8fdc6a836e005c9ddcf84518460f0e255

@RestController
@RequestMapping("userCM")
public class UserCMController {
<<<<<<< HEAD

    @Autowired
    private UserCMService userCMService;

    @GetMapping("get_user_info_by_id/{id}")
    public UserInfoDto getUserInfo(@PathVariable int id){
        return userCMService.getUserInfoById(id);
    }

    @GetMapping("search_user_chat")
    public List<UserInfoDto> searchUserChat(@RequestParam(required = false, defaultValue = "") String search, @RequestParam int userId, @RequestParam List<Long> friendsId){
        return userCMService.searchForUser(userId, search,friendsId);
    }

    @GetMapping("search_user_group_chat")
    public ResponseEntity<CustomResult> searchUserGroupChat(@RequestParam(required = false, defaultValue = "") String search, @RequestParam int userId){
        var customResult = userCMService.searchUserForGroupChat(userId, search);
        return ResponseEntity.ok(customResult);
=======
    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private PasswordEncoder _passwordEncoder;

    @Autowired
    private ImageUploadingService _imageUploadingService;

    @Autowired
    private MailService _mailService;

    @Autowired
    private AzureSender _azureSender;

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

    @PutMapping("government")
    public ResponseEntity<CustomResult> putGovernment(@ModelAttribute GovernmentRequest request) {
        CustomResult result = new CustomResult();
        try {
            // Change mock after
            String mockEmail = "bigmouth3033@gmail.com";

            var email = SecurityContextHolder.getContext().getAuthentication().getName();

            User user = _userRepository.findUserByEmail(email);
            var frontImageUrl = _imageUploadingService.upload(request.getFrontImage());
            var backImageUrl = _imageUploadingService.upload(request.getBackImage());

            // update DriverLicense
            if (request.getIdType() == 1) {
                user.setDriverLicenseFrontUrl(frontImageUrl);
                user.setDriverLicenseBackUrl(backImageUrl);
                user.setDriverLicenseCountry(request.getGovernmentCountry());
            }
            // update IdentityCard
            if (request.getIdType() == 2) {
                user.setIdentityCardFrontUrl(frontImageUrl);
                user.setIdentityCardBackUrl(backImageUrl);
                user.setIdentityCardCountry(request.getGovernmentCountry());
            }
            _userRepository.save(user);

            GovernmentResponse governmentResponse = new GovernmentResponse();
            governmentResponse.setIdType(request.getIdType());
            governmentResponse.setFrontImageUrl(frontImageUrl);
            governmentResponse.setBackImageUrl(backImageUrl);
            governmentResponse.setGovernmentCountry(request.getGovernmentCountry());
            result.setStatus(200);
            result.setMessage("Update Government Success");
            result.setData(governmentResponse);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.setStatus(400);
            result.setMessage("Some thing went wrong while update Government");
            result.setData(null);
            return ResponseEntity.badRequest().body(result);

        }
    }

    @PutMapping("changePassword")
    public ResponseEntity<CustomResult> putChangePassword(@ModelAttribute ChangePasswordRequest request) {
        CustomResult result = new CustomResult();
        try {
            // Change mock after
            String mockEmail = "bigmouth3033@gmail.com";

            var email = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = _userRepository.findUserByEmail(email);

            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                result.setStatus(404);
                result.setMessage("Password confirm doesn't match");
                result.setData(null);
                return ResponseEntity.badRequest().body(result);
            }

            if (!_passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                result.setStatus(404);
                result.setMessage("Current password doesn't match");
                result.setData(null);
                return ResponseEntity.ok(result);
            }

            String otp = OTPGenerator.generateOTP();
            user.setNewPassword(_passwordEncoder.encode(request.getNewPassword()));
            user.setOTP(otp);
            _userRepository.save(user);

            MailPayload mailPayload = new MailPayload();
            mailPayload.setFile(null);
            mailPayload.setTo(user.getEmail());
            String[] cc = {user.getEmail()};
            mailPayload.setCc(cc);

            String otpSubject = "Your OTP for Password Change Request";
            String otpNotification = "Your One-Time Password (OTP) for changing your password is: " +
                    otp +
                    " Please use this OTP to proceed with your password change. " +
                    "This OTP is valid for 10 minutes and should not be shared with anyone.";
            String emailHtml = "<html>"
                    + "<head>"
                    + "    <style>"
                    + "        body {"
                    + "            font-family: Arial, sans-serif;"
                    + "            background-color: #f4f4f4;"
                    + "            color: #333;"
                    + "            padding: 20px;"
                    + "        }"
                    + "        .container {"
                    + "            background: #fff;"
                    + "            border-radius: 8px;"
                    + "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);"
                    + "            padding: 20px;"
                    + "            max-width: 400px;"
                    + "            margin: auto;"
                    + "        }"
                    + "        .otp {"
                    + "            font-size: 24px;"
                    + "            font-weight: bold;"
                    + "            color: #007bff;"
                    + "            margin: 20px 0;"
                    + "        }"
                    + "        .note {"
                    + "            font-size: 14px;"
                    + "            color: #888;"
                    + "        }"
                    + "    </style>"
                    + "</head>"
                    + "<body>"
                    + "    <div class=\"container\">"
                    + "        <h2>Your OTP for Password Change</h2>"
                    + "        <p class=\"otp\">"
                    + otp
                    + "</p>"  // Replace with actual OTP
                    + "        <p class=\"note\">This OTP is valid for 10 minutes and should not be shared with anyone.</p>"
                    + "    </div>"
                    + "</body>"
                    + "</html>";
            mailPayload.setSubject(otpSubject);
            mailPayload.setBody(emailHtml);
            _azureSender.sendMessage(mailPayload);

            result.setStatus(200);
            result.setData(null);
            String message = String.format("Your OTP has been sent to your email %s", otp);
            result.setMessage(message);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.setStatus(400);
            result.setMessage("Some thing went wrong while update password");
            result.setData(null);
            return ResponseEntity.ok(result);
        }
>>>>>>> 190256b8fdc6a836e005c9ddcf84518460f0e255
    }
}
