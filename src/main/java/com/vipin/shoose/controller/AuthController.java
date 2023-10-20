package com.vipin.shoose.controller;
import com.vipin.shoose.dto.UserDto;
import com.vipin.shoose.dto.VerifyOtpDto;
import com.vipin.shoose.exception.MailAlreadyExistException;
import com.vipin.shoose.exception.OtpInvalidException;
import com.vipin.shoose.model.User;
import com.vipin.shoose.repository.UserRepository;
import com.vipin.shoose.service.UserService;
import com.vipin.shoose.util.OtpService;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDateTime;
import java.util.Set;



@Controller
public class AuthController {
    @Autowired
    OtpService otpService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @GetMapping("/")
    public String home(){
        return "user/home";
    }
    @GetMapping("/home")
    public String AuthRedirect(){
        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            if(roles.contains("ADMIN")){
                return "redirect:/admin/home";
            }
            if(roles.contains("USER")){
                return "redirect:/user/home";
            }
        }
        return "redirect:/user/home";
    }
    @GetMapping("/register")
    public String signUp(Model model){
        UserDto userDto=new UserDto();
        model.addAttribute("userDto",userDto);
        return "user/register";
    }
    @PostMapping("/register")
    public String registerPost(UserDto userDto, HttpSession session, RedirectAttributes redirectAttributes){
        User existingUserEmail=userRepository.findByEmail(userDto.getEmail());
        User existingUserByUsername=userRepository.findByUsername(userDto.getUsername());
        if(existingUserEmail!=null){
          String errorMessage="MailId you have entered  already exists";
          redirectAttributes.addFlashAttribute("mailError",errorMessage);
          return "redirect:/register";
        }else if(existingUserByUsername!=null){
            String errorMessage="Username you have entered  already exists";
            redirectAttributes.addFlashAttribute("mailError",errorMessage);
            return "redirect:/register";
        }
        String otp=otpService.generateOtp();
        userDto.setOtp(otp);
        userDto.setOtpGeneratedTime(LocalDateTime.now());
        otpService.sendOTPEmail(userDto.getEmail(),otp);
        session.setAttribute("unverifiedUser",userDto);
        return "redirect:/verify-otp";
    }
    @PostMapping("/login")
    public String loginPost(@RequestParam(name = "error", required=false)String error ,Model model){
        if(error!=null){
            if(error.equals("blocked")){
                model.addAttribute("You are blocked by the admin");
            }
            else if(error.equals("incorrect")){
                model.addAttribute("Incorrect username or password");
            }
        }
        try {

        } catch (org.springframework.security.authentication.DisabledException ex) {
            model.addAttribute("errorMessage", "User is blocked");
        }
        return "/home";
    }
    @GetMapping("/login")
    public String login(){
        return "user/sign-in";
    }

    @GetMapping("/shop")
    public String shop(){
        return "user/shop";
    }
    @GetMapping("/verify-otp")
    public String verifyOtp(Model model,HttpSession session){
        UserDto userDto= (UserDto) session.getAttribute("unverifiedUser");
        model.addAttribute("userOtp",new VerifyOtpDto());
        return "user/signup-otp";
    }
    @PostMapping("/verify-otp")
    public String verifySuccess(@RequestParam("otp") String otp, HttpSession session, Model model) {
        UserDto userDto = (UserDto) session.getAttribute("unverifiedUser");
        try{
           if( userService.verifyOtp(userDto, otp) ){
               return "redirect:/login";
           }
        }catch (OtpInvalidException e){
            model.addAttribute("otpError", "Invalid OTP. Please try again.");
        }

            return "user/signup-otp";

    }
    @PostMapping("/resend-otp")
    public String resendOtp(HttpSession session,RedirectAttributes redirectAttributes){
       UserDto userDto= (UserDto) session.getAttribute("unverifiedUser");
       String email=userDto.getEmail();
       String otp=otpService.generateOtp();
       otpService.resendOTPEmail(email,otp);
       userDto.setOtp(otp);
       redirectAttributes.addFlashAttribute("resend","New otp has been sen to your email");
       return "redirect:/verify-otp";
    }


}
