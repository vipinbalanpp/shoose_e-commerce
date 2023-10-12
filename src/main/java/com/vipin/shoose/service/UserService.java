package com.vipin.shoose.service;

import com.vipin.shoose.dto.UserDto;
import com.vipin.shoose.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
   public void register(UserDto userDto);

    List<User> getAllUsers();
    public void  verifyOtp(String otp);
}
