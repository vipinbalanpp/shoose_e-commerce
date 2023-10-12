package com.vipin.shoose.service;

import com.vipin.shoose.dto.UserDto;
import com.vipin.shoose.model.User;
import com.vipin.shoose.repository.UserRepository;
import com.vipin.shoose.util.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OtpService otpService;

    @Override
    public void register(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles("USER");
        user.setEnabled(true);
        user.setCreatedDate(LocalDateTime.now());
        user.setOtp(otpService.generateOtp());
        user.setOtpGeneratedTime(LocalDateTime.now());
        otpService.sendOTPEmail(userDto.getEmail(),user.getOtp());
        userRepository.save(user);
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void verifyOtp(String otp) {
        User user=userRepository.findByOtp(otp);
        if(user==null){
            throw new UsernameNotFoundException("user not found");
        }user.setVerified(true);
        userRepository.save(user);
        otpService.sendEmail(user.getEmail());
    }


}
