package com.vipin.shoose.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Usercontroller {
    @GetMapping("/login")
    public String userLogin(){
        return "user/sign-in";
    }
    @GetMapping("/recoverAccount")
    public String recoverAccount(){
        return "user/recoverAccount";
    }
    @GetMapping("/recoverOTP")
    public String recoverOTP(){
        return "user/recoverOTP";
    }
    @GetMapping("/change-password")
    public String changepassword(){
        return "user/change-password";
    }
    @GetMapping("/signUp")
    public String signUp(){
        return "user/sign-up";
    }
    @GetMapping("/user/home")
    public String home(){
        return "user/home";
    }
    @GetMapping("/shop")
    public String shop(){
        return "user/shop";
    }
    @GetMapping("/product-details")
    public String productDetails(){
        return "user/product-details";
    }

}
