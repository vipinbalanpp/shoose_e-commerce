package com.vipin.shoose.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BannerController {
    @GetMapping("admin/banners")
    public String banners(){
        return "admin/banners";
    }
}
