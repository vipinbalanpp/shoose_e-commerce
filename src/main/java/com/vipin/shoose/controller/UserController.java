package com.vipin.shoose.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.Set;

@Controller
public class UserController {
    @GetMapping("/user/home")
    public String home(Model model){

        return "user/home";
    }
}
