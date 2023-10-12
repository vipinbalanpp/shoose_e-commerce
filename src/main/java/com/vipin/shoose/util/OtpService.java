package com.vipin.shoose.util;

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
                    "Best regards from team.shoose@gamil.com");



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
            message.setText("Thank you for registering on our website. To complete your registration, please enter the following OTP: "
                    + otp
            +"This OTP is valid for a limited time. Please do not share it with anyone.     If you did not sign up for our service, please ignore this email."+
                    "If you have any questions or need assistance, please contact our support team at team.shoose@gmail.com." + "Thank you for choosing our service.        " +
                            "       Best regards," +
                            "                     Shoose");
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isOtpValid(User user, String otp) {
        LocalDateTime otpGeneratedTime = user.getOtpGeneratedTime();
        LocalDateTime currentTimestamp = LocalDateTime.now();
        return user.getOtp().equals(otp) && currentTimestamp.isBefore(otpGeneratedTime.plusMinutes(2));
    }
}
