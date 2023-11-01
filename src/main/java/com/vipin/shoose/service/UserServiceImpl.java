package com.vipin.shoose.service;
import com.vipin.shoose.dto.UserDto;
import com.vipin.shoose.exception.OtpInvalidException;
import com.vipin.shoose.model.User;
import com.vipin.shoose.repository.UserRepository;
import com.vipin.shoose.util.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    SessionRegistry sessionRegistry;
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
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        for (Object principal : allPrincipals) {
            if (principal instanceof UserDetails otherUserDetails) {
                if (otherUserDetails.getUsername().equals(user.getUsername())) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    for (SessionInformation session : sessions) {
                        session.expireNow();
                    }
                }
            }
        }
        user.setEnabled(false);
  userRepository.save(user) ;


    }

    @Override
    public void unBlockUser(Long id) {
        User user=userRepository.findByUserId(id);
        user.setEnabled(true);
        userRepository.save(user) ;
    }

    @Override
    public void sendOtp(UserDto userDto) {
        String otp=otpService.generateOtp();
        userDto.setOtp(otp);
        userDto.setOtpGeneratedTime(LocalDateTime.now());
        otpService.sendOTPEmail(userDto.getEmail(),otp);
    }

    @Override
    public void setNewPassword(UserDto userDto) {
        try {
            User user=userRepository.findByEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
        }catch (Exception e){
            throw new RuntimeException("caught error");
        }
    }

    @Override
    public void resendMail(UserDto userDto) {
        userDto.setOtp(otpService.generateOtp());
        userDto.setOtpGeneratedTime(LocalDateTime.now());
        otpService.sendOTPEmail(userDto.getEmail(),userDto.getOtp());

    }

    @Override
    public void changeUsername(User user, String newUsername) {
        user.setUsername(newUsername);
        userRepository.save(user);

    }

    @Override
    public User getCurrentUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername());
    }

    @Override
    public boolean checkCurrentPassword(User user, String oldPassword) {
        String storedPassword = user.getPassword();
        boolean passwordMatch = passwordEncoder.matches(oldPassword, storedPassword);
        System.out.println("Stored Password: " + storedPassword);
        System.out.println("Entered Password: " + oldPassword);
        System.out.println("Password Match: " + passwordMatch);
        return passwordMatch;
    }


    @Override
    public void changePassword(User user, String newPassword) {

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


}
