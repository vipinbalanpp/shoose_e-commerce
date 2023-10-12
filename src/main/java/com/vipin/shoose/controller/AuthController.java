package com.vipin.shoose.controller;

import com.vipin.shoose.dto.UserDto;
import com.vipin.shoose.dto.VerifyOtpDto;
import com.vipin.shoose.model.User;
import com.vipin.shoose.repository.UserRepository;
import com.vipin.shoose.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @GetMapping("/register")
    public String signUp(Model model){
        UserDto userDto=new UserDto();
        model.addAttribute("userDto",userDto);
        return "user/register";
    }
    @PostMapping("/register")
    public String registerPost(UserDto userDto){
        userService.register(userDto);
        return "redirect:/verify-otp";
    }
    @PostMapping("/login")
    public String loginPost(){
        return "redirect:/";
    }
    @GetMapping("/login")
    public String login(){
        return "user/sign-in";
    }
    @GetMapping("/")
    public String home(Authentication authentication){
        return "user/home";
    }
    @GetMapping("/shop")
    public String shop(){
        return "user/shop";
    }
    @GetMapping("/verify-otp")
    public String verifyOtp(Model model){
        model.addAttribute("userOtp",new VerifyOtpDto());
        return "user/signup-otp";
    }
    @PostMapping("/verify-otp")
    public String verifySuccess(@RequestParam("otp") String otp){
        userService.verifyOtp(otp);
        return "redirect:/login";
    }
}
