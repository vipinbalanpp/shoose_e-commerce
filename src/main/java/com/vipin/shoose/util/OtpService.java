package com.vipin.shoose.util;

import com.vipin.shoose.dto.UserDto;
import com.vipin.shoose.model.User;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;

@Service
public class OtpService {
    @Autowired
    JavaMailSender javaMailSender;
    public String generateOtp(){
        Random random=new Random();
        int otp=100000+random.nextInt(999999);
        return String.valueOf(otp);
    }
    public void sendEmail(String recipientEmail) {

        try {
            SimpleMailMessage message=new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Registration Success Mail");
            message.setText(" Thank you for registering with Shoose!" +
                    " Your account has been successfully created. You are now part of our community." +
                    "Best regards from team.shoose@gmail.com");



            // Send the message
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Send OTP via email
    public void sendOTPEmail(String recipientEmail, String otp) {
        try {

            SimpleMailMessage message=new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Your OTP Code");
            message.setText("Your otp from shoose app is:  "
                    + otp);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void resendOTPEmail(String recipientEmail, String otp) {
        try {

            SimpleMailMessage message=new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Your OTP Code");
            message.setText("Your new otp from to shoose app is:  "
                    + otp);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOtpValid(UserDto userDto, String otp) {
        LocalDateTime otpGeneratedTime = userDto.getOtpGeneratedTime();
        LocalDateTime currentTimestamp = LocalDateTime.now();
        return userDto.getOtp().equals(otp) && currentTimestamp.isBefore(otpGeneratedTime.plusMinutes(2));
    }
}
