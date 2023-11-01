package com.vipin.shoose.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CouponController {
    @GetMapping("admin/coupons")
    public String coupons(){
        return "admin/coupon";
    }
}
