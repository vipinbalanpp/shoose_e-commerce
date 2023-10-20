package com.vipin.shoose.service;
import com.vipin.shoose.dto.UserDto;
import com.vipin.shoose.exception.OtpInvalidException;
import com.vipin.shoose.model.User;
import com.vipin.shoose.repository.UserRepository;
import com.vipin.shoose.util.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean verifyOtp(UserDto userDto, String otp) {
        User user=new User();
        if(userDto!=null&&otpService.isOtpValid(userDto,otp)){
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setRoles("USER");
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setEnabled(true);
            user.setCreatedTime(LocalDateTime.now());
            userRepository.save(user);
            otpService.sendEmail(user.getEmail());
           return true;
        } else {
            throw  new OtpInvalidException("Invalid Otp");

        }
    }

    @Override
    public void blockUser(Long id) {
        User user=userRepository.findByUserId(id);
        user.setEnabled(false);
  userRepository.save(user) ;


    }

    @Override
    public void unBlockUser(Long id) {
        User user=userRepository.findByUserId(id);
        user.setEnabled(true);
        userRepository.save(user) ;
    }


}
