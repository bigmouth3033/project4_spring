package com.service.main.service;

import org.springframework.stereotype.Service;

@Service
public class MailBodyService {


    public String getRegisterAuthenticationMail(String authenticationCode) {

        return String.format(
                "<!DOCTYPE html>" +
                        "<html lang=\"en\">" +
                        "<head>" +
                        "<meta charset=\"UTF-8\">" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                        "<title>Complete Your Urban Nest Registration</title>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; margin: 0; padding: 20px; }" +
                        ".container { background-color: #fff; border-radius: 5px; padding: 20px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); }" +
                        "h1 { color: #4CAF50; }" +
                        ".code { font-size: 20px; font-weight: bold; color: #4CAF50; margin: 10px 0; }" +
                        ".footer { margin-top: 20px; font-size: 14px; color: #777; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class=\"container\">" +
                        "<h1>Welcome to Urban Nest!</h1>" +
                        "<p>Thank you for signing up with Urban Nest! We’re excited to have you join our community.</p>" +
                        "<p>To complete your registration, please use the following authentication code:</p>" +
                        "<div class=\"code\">%s</div>" +
                        "<p>Simply enter this code on the registration page to finalize your account setup.</p>" +
                        "<p>If you didn’t initiate this registration, please ignore this email.</p>" +
                        "<p class=\"footer\">Welcome to Urban Nest, and happy hosting!</p>" +
                        "<p class=\"footer\">Best regards,<br>The Urban Nest Team</p>" +
                        "</div>" +
                        "</body>" +
                        "</html>", authenticationCode);
    }

    public String getEmployeeWelcomeMail(String name, String employeeEmail, String employeePassword) {

        return String.format(
                "<!DOCTYPE html>" +
                        "<html lang=\"en\">" +
                        "<head>" +
                        "<meta charset=\"UTF-8\">" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                        "<title>Welcome to Urban Nest – Complete Your Registration</title>" +
                        "<style>" +
                        "body { font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; margin: 0; padding: 20px; }" +
                        ".container { background-color: #fff; border-radius: 5px; padding: 20px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); }" +
                        "h1 { color: #4CAF50; }" +
                        ".code { font-size: 20px; font-weight: bold; color: #4CAF50; margin: 10px 0; }" +
                        ".footer { margin-top: 20px; font-size: 14px; color: #777; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class=\"container\">" +
                        "<h1>Welcome to Urban Nest!</h1>" +
                        "<p>Dear %s,</p>" +
                        "<p>Thank you for joining the Urban Nest team! We’re excited to have you with us as we revolutionize the vacation rental industry.</p>" +
                        "<p>Your account details are as follows:</p>" +
                        "<ul>" +
                        "<li><strong>Email:</strong> %s</li>" +
                        "<li><strong>Password:</strong> %s</li>" +
                        "</ul>" +
                        "<p>Simply enter this email and password on the login page to finalize your account setup.</p>" +
                        "<p class=\"footer\">We’re thrilled to have you on board and look forward to your contributions to Urban Nest!</p>" +
                        "<p class=\"footer\">Best regards,<br>The Urban Nest Team</p>" +
                        "</div>" +
                        "</body>" +
                        "</html>",name, employeeEmail, employeePassword);
    }
}
